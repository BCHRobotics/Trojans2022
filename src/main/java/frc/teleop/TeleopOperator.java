package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.shoot.*;
import frc.commands.climb.*;
import frc.commands.intake.Collect;
import frc.commands.intake.Stage;
import frc.io.DriverInput;
import frc.sequences.Climb;
import frc.sequences.Shoot;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopOperator extends TeleopComponent {

    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private Collect intakeCommand;
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
    private boolean climbLatch;
    private boolean confirmClimb;

    private long previousTime;
    private long currentTime;
    private final long stagerDelay = 600;
    private final long feederDelay = 800; // 800
    // private long kickerDelay = 120;
    // private boolean kickerEnabled = false;
    // private boolean feedButtonLock = false;

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

        this.intakeCommand = Collect.getInstance();
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
        this.intakeCommand.initialize();
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
        } else if (this.operatorController.getAButton() || this.shootState) {
            if (!this.shootState) {
                this.shootState = true;
                this.shootSequence.startTimer();
            }
            this.shootSequence.calculate();
        } else if (this.operatorController.getRightBumper()) {
            this.limelightCommand.calculate();
            this.stageCommand.calculate();

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getXButton()) {
            this.stageCommand.end();
            this.feedCommand.calculate();
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
            this.intakeCommand.reverse();
            this.stageCommand.reverse();
            this.feedCommand.reverse();
        } else {
            if (!this.shootState) {
                this.idleMode();
                this.previousTime = this.currentTime;
            } else if (this.shootState && (this.currentTime >= (this.previousTime + this.stagerDelay + this.feederDelay)) && !this.shootLatch) {
                this.idleMode();
                this.shootState = false;
            } else if (this.shootState && (this.currentTime >= (this.previousTime + this.stagerDelay)) && !this.shootLatch) {
                this.launchCommand.calculate();
            }
        }

        this.climbSwing.setArmPosition(1);
        this.manualShootCommand.setShooterSpeed(SmartDashboard.getNumber("Shooter Wheels", 0));
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

        if (this.climbLatch) {
            this.climbSwing.setArmPosition(this.operatorController.getJoystick(Side.LEFT, Axis.Y));
            this.climbLift.setArmHeight(this.operatorController.getJoystick(Side.RIGHT, Axis.Y));
        }
        SmartDashboard.putBoolean("CLIMB CONFIRMED", this.confirmClimb);
        this.climbSequence.calculate();
    }

    private void execute() {
        this.intakeCommand.execute();
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
        this.intakeCommand.disable();
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