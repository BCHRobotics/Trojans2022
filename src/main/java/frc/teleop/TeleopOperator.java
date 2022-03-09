package frc.teleop;

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
    private long feederDelay = 800;

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

        SmartDashboard.putBoolean("Intake State", false);
        SmartDashboard.putNumber("Intake Rollers", 0);
        SmartDashboard.putNumber("Stager Rollers", 0);
        SmartDashboard.putNumber("Feeder Belt", 0);
        SmartDashboard.putBoolean("Feeder State", false);
        SmartDashboard.putNumber("Shooter Wheels", 3000);
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
                this.climber.setRobotArmPosition(this.armClimb);
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
        if (this.operatorController.getAButton()) {
            this.limelight.setLedMode(3);
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

            this.climber.setRobotArmPosition(this.armShoot);
            this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
            this.intake.setStagerSpeed(1);

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getLeftBumper()) {
            this.shooterWheelRPM = SmartDashboard.getNumber("Shooter Wheels", 0);

            this.climber.setRobotArmPosition(Constants.ANGLE_LIMIT);
            this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
            this.intake.setStagerSpeed(0);

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getLeftTriggerAxis() > 0.5) {
            this.shooterWheelRPM = 1260;

            this.climber.setRobotArmPosition(Constants.ANGLE_LIMIT);
            this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
            this.intake.setStagerSpeed(0);

            this.shootState = true;
            this.shootLatch = true;
        } else if (this.operatorController.getYButton()) {
            this.intake.setStagerSpeed(0);
            this.intake.setFeederSpeed(0);
            this.intake.setFeederState(false);
            this.climber.setRobotArmPosition(Constants.ANGLE_LIMIT);
            this.shootState = false;
            this.shootLatch = false;
        } else if (this.operatorController.getXButton()) {
            this.intake.setFeederState(true);
            this.previousTime = this.currentTime;
            this.shootState = true;
            this.shootLatch = false;
        } else if (operatorController.getRightBumper()) {
            this.shootState = true;
            this.shootLatch = true;
            this.intake.setStagerSpeed(1);
            this.intake.setFeederSpeed(0.5);
            this.intake.setFeederState(false);
            this.drive.lockPosition(false);
        } else if (this.operatorController.getBButton()) {
            this.intake.setFeederSpeed(-0.5);
            this.intake.setStagerSpeed(-1);
            this.intake.setIntakeSpeed(-1);
        } else {
            if (!this.shootState) {
                this.previousTime = this.currentTime;
                this.shooter.setShooterWheelSpeed(0);
                this.limelight.setLedMode(1);
                this.climber.setRobotArmPosition(armShoot);
                this.intake.setFeederState(false);
                this.intake.setFeederSpeed(0);
            } else if (this.shootState && (this.currentTime >= (previousTime + feederDelay)) && !this.shootLatch) {
                this.intake.setStagerSpeed(0);
                this.intake.setFeederSpeed(0);
                this.limelight.setLedMode(1);
                this.intake.setFeederState(false);
                this.climber.setRobotArmPosition(Constants.ANGLE_LIMIT);
                this.shootState = false;
            }
            this.drive.lockPosition(false);
        }
        
        // this.shooterWheelRPM = SmartDashboard.getNumber("Shooter Wheels", 0);
        // this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);

        // Algorithim to calculate and aim shooter from limelight value

        //this.shooter.setShooterWheelSpeed(this.operatorController.getJoystick(Side.LEFT, Axis.Y)*0.8);
        //this.shooter.setShooterWheelSpeed(SmartDashboard.getNumber("SHOOTER VAL", 0));
        //this.climber.setClimberWinchPosition(0);
    }

    public void oldLimelightSeek() {
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

    public void limelightSeek() {
        this.drive.lockPosition(true);
        this.tx = this.limelight.getTargetX();
        SmartDashboard.putNumber("Limelight X", this.tx);

        double driveRevolutionsLeft = (this.tx / conversionLeft);
        double driveRevolutionsRight = (this.tx / conversionRight);

        this.drive.resetPosition();
        this.drive.setDriveLeft(driveRevolutionsLeft);
        this.drive.setDriveRight(driveRevolutionsRight);
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        // Set Climber winch position constant
        final double climberWinchRotations = 120;

        // Activate Lift based on joystick and constants multiplyer
        this.climber.setRobotArmPosition(this.armClimb);

        this.shooter.setShooterWheelSpeed(0);

        // Activate Climber based on joystick and constants multiplyer
        this.climber.setClimberWinchPosition(this.operatorController.getJoystick(Side.RIGHT, Axis.Y) * climberWinchRotations);
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
        this.climber.disable();
    }
}