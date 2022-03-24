package frc.teleop;

import java.nio.file.FileSystemAlreadyExistsException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.DriverInput;
import frc.io.subsystems.ShooterIO;
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
import frc.util.math.Trajectory;

public class TeleopOperator extends TeleopComponent {

    private static TeleopOperator instance;

    private Controller operatorController;
    private Trajectory trajectoy;
    private DriverInput driverInput;
    private Limelight limelight;

    // Auto shooting variable
    private double distance;
    private double height;
    private double angle;
    private double velocity;
    private double stagerSpeed;
    private double feederSpeed;
    private boolean feederState;
    private double shooterWheelRPM;
    private double climberArmRevolutions;
    private double tx;
    private boolean shootState;
    private boolean shootLatch;

    private double kP = 0.06;
    private double minOutput = 0.0;

    final double conversionLeft = 11.2835522805; //12.51653
    final double conversionRight = -9.4263945489; //-8.5326

    private long previousTime;
    private long currentTime;
    private long feederDelay = 500; //800
    // private long kickerDelay = 120;
    // private boolean kickerEnabled = false;
    // private boolean feedButtonLock = false;

    private double armClimb = 0;
    private double armShoot = Constants.ANGLE_LIMIT; //40

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

        this.trajectoy = new Trajectory();
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

        SmartDashboard.putBoolean("Intake State", false);
        SmartDashboard.putNumber("Intake Rollers", 0);
        SmartDashboard.putNumber("Stager Rollers", 0);
        SmartDashboard.putNumber("Feeder Belt", 0);
        SmartDashboard.putBoolean("Feeder State", false);
        SmartDashboard.putNumber("Shooter Wheels", 2000);
        SmartDashboard.putBoolean("Arm State", false);
        SmartDashboard.putNumber("Arm pos", this.armShoot);
        SmartDashboard.putNumber("Drive Rotate", 0);
        SmartDashboard.putNumber("Distance", 0);

        SmartDashboard.putNumber("Limelight kP", this.kP);
        SmartDashboard.putNumber("Limelight minOut", this.minOutput);

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
            if (operatorMode == OperatorMode.SHOOT) {
                operatorMode = OperatorMode.CLIMB;
                this.firstCycle();
                this.climber.setRobotArmPosition(this.armClimb);
            } else if (operatorMode == OperatorMode.CLIMB) {
                operatorMode = OperatorMode.SHOOT;
                this.firstCycle();
                this.climber.setRobotArmPosition(this.armShoot);
            }
        }

        this.shooter.calculate();
        this.climber.calculate();
        this.intake.calculate();
        this.drive.calculate();
    }

    private void testShootMode() {
        this.intake.setIntakeState(SmartDashboard.getBoolean("Intake State", false));
        this.intake.setIntakeSpeed(SmartDashboard.getNumber("Intake Rollers", 0));
        this.intake.setStagerSpeed(SmartDashboard.getNumber("Stager Rollers", 0));
        this.intake.setFeederSpeed(SmartDashboard.getNumber("Feeder Belt", 0));
        this.intake.setFeederState(SmartDashboard.getBoolean("Feeder State", false));
        this.shooter.setShooterWheelSpeed(SmartDashboard.getNumber("Shooter Wheels", 0));
        this.drive.setDriveLeft(SmartDashboard.getNumber("Drive Rotate", 0));
        this.drive.setDriveRight(-SmartDashboard.getNumber("Drive Rotate", 0));
        if (SmartDashboard.getBoolean("Arm State", false)) {
            this.climber.setRobotArmPosition(this.armShoot);
        } else {
            this.climber.setRobotArmPosition(this.armClimb);
        }
        this.climber.setRobotArmPosition(SmartDashboard.getNumber("Arm pos", 0));
    }

    private void shootMode() {
        this.currentTime = System.currentTimeMillis();
        
        // Algorithim to hunt for target and latch on to it
        this.armShoot = SmartDashboard.getNumber("Arm pos", 0);
        
        if (this.operatorController.getRightBumper()) {
            this.limelight.setLedMode(3);
            this.drive.setLockPosition(true);
            this.drive.setBrakeMode(true);
            this.drive.brake(true);
            this.limelightSeek();
            this.distance = this.limelight.getTargetDistance();
            this.height = Constants.TARGET_HEIGHT - Constants.SHOOTER_HEIGHT;

            this.trajectoy.setDistance(this.distance);
            this.trajectoy.setHeight(this.height);

            this.angle = trajectoy.getAngle();
            this.velocity = trajectoy.getVelocity();

            // Converts velocity into RPM: [(Velocity x 60<seconds>) / (PI * Diameter<meters>)]
            //this.shooterWheelRPM = (this.velocity * 60) / (Math.PI * 0.115062);
            //this.climberArmRevolutions = (this.angle / 360) * Constants.LIFT_ARM_GEAR_REDUCTION;

            this.stagerSpeed = 1;
            this.feederSpeed = 1;
            this.shooterWheelRPM = calculateShooterWheelRPM(this.distance);

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getXButton()) {
            // if (!this.kickerEnabled && !this.feedButtonLock) {
            //     this.feederState = true;
            //     this.previousTime = this.currentTime;
            //     this.kickerEnabled = true;
            //     this.shootState = true;
            //     this.shootLatch = false;
            //     this.feedButtonLock = true;
            // } else if (this.currentTime >= (this.previousTime + this.kickerDelay)) {
            //     this.feederState = false;
            //     this.kickerEnabled = false;
            // }

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
            this.shooterWheelRPM = 1100;

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
            } else if (this.shootState && (this.currentTime >= (this.previousTime + this.feederDelay)) && !this.shootLatch) {
                this.stagerSpeed = 0;
                this.feederSpeed = 0;
                this.feederState = false;
                this.shootState = false;
                // this.kickerEnabled = false;
                this.drive.brake(false);
                this.drive.setBrakeMode(false);
            }
            // this.feedButtonLock = false;
            this.drive.setLockPosition(false);
        }

        this.intake.setStagerSpeed(this.stagerSpeed);
        this.intake.setFeederSpeed(this.feederSpeed);
        this.intake.setFeederState(this.feederState);
        this.climber.setRobotArmPosition(this.armShoot);
        this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
        
        // this.shooterWheelRPM = SmartDashboard.getNumber("Shooter Wheels", 0);
        // this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
    } 

    private void oldLimelightSeek() {
        this.tx = this.limelight.getTargetX();
        double headingError = this.tx;
        double turningAmount = 0.0;
        
        SmartDashboard.putNumber("Limelight X", this.tx);

        this.kP = SmartDashboard.getNumber("Limelight kP", 0);
        this.minOutput = SmartDashboard.getNumber("Limelight minOut", 0);

        if (tx > 1.0) {
            turningAmount = (this.kP * headingError) - this.minOutput;
        } else if (tx < 1.0) {
            turningAmount = (this.kP * headingError) + this.minOutput;
        }
        
        if (this.tx < 1 && this.tx > -1) return;

        this.drive.setOutput(0, turningAmount);
    }

    private void limelightSeek() {
        this.tx = this.limelight.getTargetX();
        SmartDashboard.putNumber("Limelight X", this.tx);

        double driveRevolutionsLeft = (this.tx / conversionLeft);
        double driveRevolutionsRight = (this.tx / conversionRight);

        this.drive.resetPosition();
        this.drive.setDriveLeft(driveRevolutionsLeft);
        this.drive.setDriveRight(driveRevolutionsRight);
    }

    private double calculateShooterWheelRPM(double setDistance) {
        final double a = 24;
        final double b = 14.4;
        final double c = 1908;
        double output  = (a*Math.pow(setDistance, 2)) + (b*setDistance) + c;
        return output;
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        // Set Climber winch position constant
        final double climberWinchRotations = 120;

        // Activate Lift based on joystick and constants multiplyer
        //this.climber.setRobotArmPosition(this.operatorController.getJoystick(Side.LEFT, Axis.Y) * Constants.ANGLE_LIMIT);
        this.climber.setRobotArmPosition(this.armClimb);

        this.shooter.setShooterWheelSpeed(0);

        // Activate Climber based on joystick and constants multiplyer
        this.climber.setClimberWinchPosition(this.operatorController.getJoystick(Side.RIGHT, Axis.Y) * climberWinchRotations);
    }

    private void resetVariables() {
        this.distance = 0;
        this.height = 0;
        this.angle = 0;
        this.velocity = 0;
        this.stagerSpeed = 0;
        this.feederSpeed = 0;
        this.feederState = false;
        this.shooterWheelRPM = 0;
        this.climberArmRevolutions = 0;
        this.tx = 0;
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