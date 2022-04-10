package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.shoot.*;
import frc.commands.climb.*;
import frc.commands.intake.Stage;
import frc.io.DriverInput;
import frc.sequences.Climb;
import frc.sequences.Shoot;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;
import frc.util.math.Arithmetic;

public class TeleopOperator extends TeleopComponent {

    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private Stage stageCommand;
    private Poly feedCommand;
    private Fire launchCommand;
    private Aim limelightCommand;
    private Manual manualShootCommand;
    private Lift climbLift;
    private Swing climbSwing;
    private Shoot shootSequence;
    private Climb climbSequence;

    // Auto shooting variable
    private boolean shootState;
    private boolean shootLatch;
    private boolean shootAutomate;
    private boolean climbLatch;
    private boolean confirmClimb;

    private double manualArmIncrement;
    private double manualWinchIncrement;

    private long previousTime;
    private long currentTime;
    private final long stagerDelay = 800;

    private enum OperatorMode {
        SHOOT, CLIMB
    }

    private OperatorMode operatorMode = OperatorMode.SHOOT;

    /**
     * Get the instance of the TeleopOperator, if none create a new instance
     * 
     * @return instance of the TeleopOperator
     */
    public static TeleopOperator getInstance() {
        if (instance == null) {
            instance = new TeleopOperator();
        }
        return instance;
    }

    private TeleopOperator() {
        this.driverInput = DriverInput.getInstance();

        this.stageCommand = Stage.getInstance();
        this.feedCommand = Poly.getInstance();
        this.launchCommand = Fire.getInstance();
        this.limelightCommand = Aim.getInstance();
        this.manualShootCommand = Manual.getInstance();
        this.climbLift = Lift.getInstance();
        this.climbSwing = Swing.getInstance();
        this.shootSequence = Shoot.getInstance();
        this.climbSequence = Climb.getInstance();

        this.operatorController = driverInput.getOperatorController();
    }

    @Override
    public void firstCycle() {
        this.stageCommand.initialize();
        this.feedCommand.initialize();
        this.launchCommand.initialize();
        this.limelightCommand.initialize();
        this.manualShootCommand.initialize();
        this.climbLift.initialize();
        this.climbSwing.initialize();
        this.shootSequence.initialize();
        this.climbSequence.initialize();
        
        this.climbLift.end();
        this.climbSwing.end();
        this.climbSequence.end();

        SmartDashboard.putNumber("Shooter Wheels", 2000);
        SmartDashboard.putNumber("Distance", 0);
    }

    @Override
    public void run() {
        switch (operatorMode) {
            case SHOOT:
                shootMode();
                break;
            case CLIMB:
                climbMode();
                break;
            default:
                shootMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            this.firstCycle();
            if (operatorMode == OperatorMode.SHOOT) {
                operatorMode = OperatorMode.CLIMB;
                this.confirmClimb = true;
            } else if (operatorMode == OperatorMode.CLIMB) {
                operatorMode = OperatorMode.SHOOT;
            }
        }

        this.execute();
    }

    private void shootMode() {
        this.currentTime = System.currentTimeMillis();

        // Algorithim to hunt for target and latch on to it

        if (this.operatorController.getYButton()) {
            this.idleMode();

            this.shootState = false;
            this.shootLatch = false;
            this.shootAutomate = false;
        } else if (this.operatorController.getAButton() || this.shootAutomate) {
            if (!this.shootAutomate) this.shootAutomate = true;
            this.shootSequence.calculate();
            this.shootState = true;
            this.previousTime = currentTime;
            if (this.shootSequence.isFinished()) this.shootAutomate = false;
        } else if (this.operatorController.getRightBumper()) {
            this.limelightCommand.calculate();
            this.stageCommand.calculate();

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getXButton()) {
            this.stageCommand.end();
            this.feedCommand.calculate();
            this.launchCommand.calculate();
            this.previousTime = this.currentTime;
            this.shootState = true;
            this.shootLatch = false;
        } else if (this.operatorController.getLeftBumper()) {
            this.manualShootCommand.setShooterSpeed(2350);
            this.stageCommand.end();

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getLeftTriggerAxis() > 0.5) {
            this.manualShootCommand.setShooterSpeed(1050);
            this.stageCommand.end();

            this.shootState = true;
            this.shootLatch = true;
        } else if (operatorController.getRightTriggerAxis() > 0.5) {
            this.stageCommand.calculate();
            this.feedCommand.calculate();
            this.launchCommand.end();

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getBButton()) {
            this.stageCommand.reverse();
            this.feedCommand.reverse();
        } else {
            if (!this.shootState) {
                this.idleMode();
                this.previousTime = this.currentTime;
            } else if (this.shootState && (this.currentTime >= (this.previousTime + this.stagerDelay)) && !this.shootLatch) {
                this.idleMode();
                this.shootState = false;
                this.shootLatch = false;
            }
        }

        this.climbSwing.setArmPosition(1);
        //this.manualShootCommand.setShooterSpeed(SmartDashboard.getNumber("Shooter Wheels", 0));

        this.stageCommand.cargoPresent();
        this.stageCommand.colorMatches();
    }

    private void idleMode() {
        this.stageCommand.end();
        this.feedCommand.end();
        this.launchCommand.end();
        this.limelightCommand.end();
        this.manualShootCommand.end();
        this.shootSequence.end();
        this.climbSequence.end();
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        if (this.operatorController.getAButton()) {
            if (this.confirmClimb) {
                this.confirmClimb = false;
                this.climbSequence.advance();
            }
        } else if (this.operatorController.getBButton()) {
            if (this.confirmClimb) {
                this.confirmClimb = false;
                this.climbSequence.reverse();
            }
        } else if (this.operatorController.getXButton()) {
            this.climbLatch = !this.climbLatch;
        } else if (this.operatorController.getYButton()) {
            this.confirmClimb = true;
        }

        SmartDashboard.putBoolean("CLIMB CONFIRMED", this.confirmClimb);
        SmartDashboard.putBoolean("CLIMB MANUAL", this.climbLatch);
        this.climbSequence.calculate();

        if (this.climbLatch) {
            this.manualArmIncrement += this.operatorController.getJoystick(Side.LEFT, Axis.Y) * 0.02;
            this.manualWinchIncrement += this.operatorController.getJoystick(Side.RIGHT, Axis.Y) * 0.02;

            this.manualArmIncrement = Arithmetic.constrain(this.manualArmIncrement, 0.0, 1.0);
            this.manualWinchIncrement = Arithmetic.constrain(this.manualWinchIncrement, 0.0, 1.0);

            SmartDashboard.putNumber("Arm Swing %", this.manualArmIncrement * 100);
            SmartDashboard.putNumber("Arm Extend %", this.manualWinchIncrement * 100);

            this.climbSwing.setArmPosition(this.manualArmIncrement);
            this.climbLift.setArmHeight(this.manualWinchIncrement);

            this.climbSwing.calculate();
            this.climbLift.calculate();
        }
    }

    private void execute() {
        this.stageCommand.execute();
        this.feedCommand.execute();
        this.launchCommand.execute();
        this.limelightCommand.execute();
        this.manualShootCommand.execute();
        this.climbLift.execute();
        this.climbSwing.execute();
        this.shootSequence.execute();
        this.climbSequence.execute();
    }

    @Override
    public void disable() {
        this.stageCommand.disable();
        this.feedCommand.disable();
        this.launchCommand.disable();
        this.limelightCommand.disable();
        this.manualShootCommand.disable();
        this.climbLift.disable();
        this.climbSwing.disable();
        this.shootSequence.disable();
        this.climbSequence.disable();
    }
}