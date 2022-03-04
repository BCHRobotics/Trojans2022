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
import frc.util.pid.PIDF;

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
    private boolean feedRoll;

    private long previousTime;
    private long currentTime;
    private long feederDelay = 800;

    private final double armClimb = 25;
    private final double armShoot = 0;

    private PIDF limelightPID;

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

        this.limelightPID = new PIDF(Constants.LIMELIGHT_ROTATE);
        this.limelightPID.setMinDoneCycles(10);
        this.limelightPID.setMaxOutput(0.2);
        this.limelightPID.setIRange(10);

        SmartDashboard.putBoolean("Intake State", false);
        SmartDashboard.putNumber("Intake Rollers", 0);
        SmartDashboard.putNumber("Stager Rollers", 0);
        SmartDashboard.putNumber("Feeder Belt", 0);
        SmartDashboard.putBoolean("Feeder State", false);
        SmartDashboard.putNumber("Shooter Wheels", 0);
        SmartDashboard.putBoolean("Arm State", false);
        SmartDashboard.putNumber("Arm pos", 0);
        SmartDashboard.putNumber("Drive Rotate", 0);
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
            } else if (operatorMode == OperatorMode.CLIMB) {
                operatorMode = OperatorMode.SHOOT;
                this.firstCycle();
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
            this.shooterWheelRPM = (this.velocity * 60) / (Math.PI * 0.115062);
            //this.climberArmRevolutions = (this.angle / 360) * Constants.LIFT_ARM_GEAR_REDUCTION;

            this.climber.setRobotArmPosition(this.armShoot);
            this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
            this.intake.setStagerSpeed(1);

            this.feedRoll = true;

            this.shootState = true;
        } else if (this.operatorController.getYButton()) {
            this.shootState = false;
            this.feedRoll = false;
        } else if (this.operatorController.getXButton()) {
            this.shootState = true;
            this.previousTime = this.currentTime;
            this.intake.setStagerSpeed(0);
            this.intake.setFeederState(true);
            this.feedRoll = true;
        } else {
            if (!this.shootState) {
                this.previousTime = this.currentTime;
                this.shooter.setShooterWheelSpeed(1000);
                this.limelight.setLedMode(1);
                this.climber.setRobotArmPosition(0);
                this.intake.setFeederState(false);
                this.feedRoll = false;
            }
        }

        if (feedRoll) {
            this.intake.setFeederSpeed(0.4);
        } else {
            this.intake.setFeederSpeed(0);
        }

        if (this.shootState && (this.currentTime >= (previousTime + feederDelay))) {
            this.intake.setStagerSpeed(0);
            this.intake.setFeederSpeed(0);
            this.intake.setFeederState(false);
            this.shootState = false;
        }

        // Algorithim to calculate and aim shooter from limelight value

        //this.shooter.setShooterWheelSpeed(this.operatorController.getJoystick(Side.LEFT, Axis.Y)*0.8);
        //this.shooter.setShooterWheelSpeed(SmartDashboard.getNumber("SHOOTER VAL", 0));
        //this.climber.setClimberWinchPosition(0);
    }

    public void oldLimelightSeek() {
        this.tx = this.limelight.getTargetX();
        SmartDashboard.putNumber("Limelight X", this.tx);
        if (this.tx < 2 && this.tx > -2) return;

        this.limelightPID.setMinMaxOutput(-0.4, 0.4);
        this.limelightPID.setDesiredValue(0);
        
        double output = this.limelightPID.calcPID(this.tx);
        this.drive.setOutput(0, -output);
    }

    public void limelightSeek() {
        this.tx = this.limelight.getTargetX();
        SmartDashboard.putNumber("Limelight X", this.tx);
        if (this.tx < 2 && this.tx > -2) return;

        double conversion = 9;
        double driveRevolutions = (this.tx / conversion);

        this.drive.setDriveLeft(10);
        this.drive.setDriveRight(-10);
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