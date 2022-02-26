package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.DriverInput;
import frc.robot.Constants;
import frc.subsystems.Shooter;
import frc.subsystems.Climber;
import frc.subsystems.Drive;
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
    private double seekError;
    private double kP = 0.085;
    private double minOutput = 0.05;

    private PIDF limelightPID;
    private boolean shooterToggle;

    private enum OperatorMode {
        SHOOT, CLIMB
    }

    private OperatorMode operatorMode = OperatorMode.CLIMB;

    private Drive drive;
    private Shooter shooter;
    private Climber climber;

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

        this.trajectoy = new Trajectory();
        this.limelight = Limelight.getInstance();

        this.operatorController = driverInput.getOperatorController();
    }

    @Override
    public void firstCycle() {
        this.drive.firstCycle();
        this.shooter.firstCycle();
        this.climber.firstCycle();

        this.limelight.setLedMode(0);
        this.limelight.setLimelightState(LimelightTargetType.UPPER_HUB);

        this.limelightPID = new PIDF(Constants.LIMELIGHT_ROTATE);
        this.limelightPID.setMinDoneCycles(10);
        this.limelightPID.setMaxOutput(0.2);
        this.limelightPID.setIRange(10);
        SmartDashboard.putNumber("SHOOTER VAL", 0);
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

        this.drive.calculate();
        this.shooter.calculate();
        this.climber.calculate();
    }

    private void shootMode() {
        this.distance = this.limelight.getTargetDistance();
        this.height = Constants.TARGET_HEIGHT - Constants.SHOOTER_HEIGHT;

        this.trajectoy.setDistance(this.distance);
        this.trajectoy.setHeight(this.height);

        this.angle = trajectoy.getAngle();
        this.velocity = trajectoy.getVelocity();

        // Converts velocity into RPM: [(Velocity x 60<seconds>) / (PI * Diameter<meters>)]
        this.shooterWheelRPM = (this.velocity * 60) / (Math.PI * 0.1016);
        this.climberArmRevolutions = (this.angle / 360) * Constants.LIFT_ARM_GEAR_REDUCTION;

        // Algorithim to hunt for target and latch on to it
        if (this.operatorController.getAButton()) {
            this.limelightSeek();
        }

        // Algorithim to calculate and aim shooter from limelight value
        if (this.operatorController.getXButton()) {
            this.climber.setRobotArmPosition(this.climberArmRevolutions);
            System.out.println(this.climberArmRevolutions);
            //this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
            System.out.println(this.shooterWheelRPM);
        } else {
            this.climber.setRobotArmPosition(65.625);
            //this.shooter.setShooterWheelSpeed(1200);
        }

        //this.shooter.setShooterWheelSpeed(this.operatorController.getJoystick(Side.LEFT, Axis.Y)*0.8);
        this.shooter.setShooterWheelSpeed(SmartDashboard.getNumber("SHOOTER VAL", 0));
        this.climber.setClimberWinchPosition(0);
    }

    public void limelightSeek() {
        this.tx = this.limelight.getTargetX();

        if (this.tx < 2 && this.tx > -2) return;

        this.limelightPID.setMinMaxOutput(-0.4, 0.4);
        this.limelightPID.setDesiredValue(0);
        
        double output = this.limelightPID.calcPID(this.tx);
        this.drive.setOutput(0, -output);
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        // Set Lift arm position constant
        final double liftArmRotations = 65.625;

        // Set Climber winch position constant
        final double climberWinchRotations = 183.35;

        // Activate Lift based on joystick and constants multiplyer
        this.climber.setRobotArmPosition(this.operatorController.getJoystick(Side.LEFT, Axis.Y) * liftArmRotations);

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