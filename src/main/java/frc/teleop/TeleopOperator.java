package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.DriverInput;
import frc.robot.Constants;
import frc.subsystems.Shooter;
import frc.subsystems.Climber;
import frc.subsystems.Drive;
import frc.subsystems.Intake;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;
import frc.util.imaging.Limelight;
import frc.util.imaging.Limelight.LimelightTargetType;

public class TeleopOperator extends TeleopComponent {

    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;
    private Limelight limelight;

    // Auto shooting variable
    private double distance;
    private double stagerSpeed;
    private double feederSpeed;
    private boolean feederState;
    private double shooterWheelRPM;
    private double climberWinchPosition;
    private double climberArmPosition;
    private boolean shootState;
    private boolean shootLatch;

    private long previousTime;
    private long currentTime;
    private long feederDelay = 500; // 800
    // private long kickerDelay = 120;
    // private boolean kickerEnabled = false;
    // private boolean feedButtonLock = false;

    private enum OperatorMode {
        SHOOT, CLIMB
    }

    private OperatorMode operatorMode = OperatorMode.SHOOT;

    private Drive drive;
    private Shooter shooter;
    private Climber climber;
    private Intake intake;

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

        this.drive = Drive.getInstance();
        this.shooter = Shooter.getInstance();
        this.climber = Climber.getInstance();
        this.intake = Intake.getInstance();

        this.limelight = Limelight.getInstance();

        this.operatorController = driverInput.getOperatorController();
    }

    @Override
    public void firstCycle() {
        this.drive.firstCycle();
        this.shooter.firstCycle();
        this.climber.firstCycle();
        this.intake.firstCycle();

        this.limelight.setLedMode(1);
        this.limelight.setLimelightState(LimelightTargetType.UPPER_HUB);

        this.resetVariables();

        SmartDashboard.putNumber("Shooter Wheels", 2000);
        SmartDashboard.putNumber("Distance", 0);
    }

    @Override
    public void calculate() {
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
            this.resetVariables();
            if (operatorMode == OperatorMode.SHOOT) {
                operatorMode = OperatorMode.CLIMB;
            } else if (operatorMode == OperatorMode.CLIMB) {
                operatorMode = OperatorMode.SHOOT;
            }
        }

        this.runCycle();

        this.shooter.calculate();
        this.climber.calculate();
        this.intake.calculate();
        this.drive.calculate();
    }

    private void shootMode() {
        this.currentTime = System.currentTimeMillis();

        // Algorithim to hunt for target and latch on to it

        if (this.operatorController.getRightBumper()) {
            this.limelight.setLedMode(3);
            this.drive.setPositionMode(true);
            this.drive.brake(true);
            this.drive.seekTarget(this.limelight.getTargetX());
            this.distance = this.limelight.getTargetDistance();

            this.stagerSpeed = 1;
            this.feederSpeed = 1;
            this.shooterWheelRPM = this.shooter.calculateShooterRPM(this.distance);

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getXButton()) {
            this.feederState = true;
            this.previousTime = this.currentTime;
            this.shootState = true;
            this.shootLatch = false;
        } else if (this.operatorController.getLeftBumper()) {
            this.shooterWheelRPM = 2350;
            this.stagerSpeed = 0;

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getLeftTriggerAxis() > 0.5) {
            this.shooterWheelRPM = 1050;

            this.stagerSpeed = 0;

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getYButton()) {
            this.stagerSpeed = 0;
            this.feederSpeed = 0;
            this.feederState = false;

            this.shootState = false;
            this.shootLatch = false;
        } else if (operatorController.getRightTriggerAxis() > 0.5) {
            this.shootState = true;
            this.shootLatch = true;
            this.stagerSpeed = 1;
            this.feederSpeed = 1;
            this.feederState = false;
        } else if (this.operatorController.getBButton()) {
            this.stagerSpeed = -1;
            this.feederSpeed = -1;
            this.intake.setIntakeSpeed(-1);
        } else {
            if (!this.shootState) {
                this.limelight.setLedMode(1);
                this.previousTime = this.currentTime;
                this.shooterWheelRPM = 0;
                this.stagerSpeed = 0;
                this.feederSpeed = 0;
                this.feederState = false;
                // this.kickerEnabled = false;
            } else if (this.shootState && (this.currentTime >= (this.previousTime + this.feederDelay))
                    && !this.shootLatch) {
                this.stagerSpeed = 0;
                this.feederSpeed = 0;
                this.feederState = false;
                this.shootState = false;
                // this.kickerEnabled = false;
                this.drive.brake(false);
            }
            // this.feedButtonLock = false;
            this.drive.setPositionMode(false);
        }

        this.climberArmPosition = Constants.ANGLE_LIMIT;
        // this.shooterWheelRPM = SmartDashboard.getNumber("Shooter Wheels", 0);
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        // Activate Lift based on joystick and constants multiplyer
        // this.climber.setRobotArmPosition(this.operatorController.getJoystick(Side.LEFT,
        // Axis.Y) * Constants.ANGLE_LIMIT);
        this.climberArmPosition = 0;

        // Activate Climber based on joystick and constants multiplyer
        this.climberWinchPosition = this.operatorController.getJoystick(Side.RIGHT, Axis.Y)
                * Constants.CLIMBER_WINCH_ROTATIONS;
    }

    private void runCycle() {
        this.intake.setStagerSpeed(this.stagerSpeed);
        this.intake.setFeederSpeed(this.feederSpeed);
        this.intake.setFeederState(this.feederState);
        this.climber.setRobotArmPosition(this.climberArmPosition);
        this.climber.setClimberWinchPosition(this.climberWinchPosition);
        this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
    }

    private void resetVariables() {
        this.distance = 0;
        this.stagerSpeed = 0;
        this.feederSpeed = 0;
        this.feederState = false;
        this.shooterWheelRPM = 0;
        this.climberArmPosition = 0;
        this.climberWinchPosition = 0;
        this.shootState = false;
        this.shootLatch = false;
        this.previousTime = 0;
        this.currentTime = 0;
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
        this.climber.disable();
    }
}