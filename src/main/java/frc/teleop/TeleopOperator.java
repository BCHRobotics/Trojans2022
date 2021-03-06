package frc.teleop;

import frc.imaging.Limelight;
import frc.imaging.Limelight.LimelightTargetType;
import frc.io.DriverInput;
import frc.robot.Constants;
import frc.subsystems.Shooter;
import frc.subsystems.Climber;
import frc.subsystems.Drive;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;
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
    private double seekError;
    private double kP = -0.1;
    private double minOutput = 0.05;

    private enum OperatorMode {
        SHOOT, CLIMB
    }

    private OperatorMode operatorMode = OperatorMode.SHOOT;

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

        this.operatorController = driverInput.getDriverController();
    }

    @Override
    public void firstCycle() {
        this.drive.firstCycle();
        this.shooter.firstCycle();
        this.climber.firstCycle();

        this.limelight.setLedMode(0);
        this.limelight.setLimelightState(LimelightTargetType.UPPER_HUB);
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
            if (operatorMode == OperatorMode.SHOOT)
                operatorMode = OperatorMode.CLIMB;
            if (operatorMode == OperatorMode.CLIMB)
                operatorMode = OperatorMode.SHOOT;
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

        this.shooterWheelRPM = (this.velocity * 60) / (Math.PI * 0.1016);
        this.climberArmRevolutions = (this.angle / 360) * Constants.LIFT_ARM_GEAR_REDUCTION;

        if (this.operatorController.getYButton()) {
            this.climber.setRobotArmPosition(this.climberArmRevolutions);
            this.shooter.setShooterWheelSpeed(this.shooterWheelRPM);
        } else {
            this.climber.setRobotArmPosition(65.625);
            this.shooter.setShooterWheelSpeed(1200);
        }

        // Algorithim to hunt for target and latch on to it
        this.tx = this.limelight.getTargetX();

        if (this.tx > 1.0)
            this.seekError = (tx * this.kP) - this.minOutput;
        else if (this.tx < 1.0)
            this.seekError = (tx * this.kP) + this.minOutput;

        if (this.operatorController.getAButton()) {
            this.drive.setOutput(0, this.seekError);
        }
    }

    /**
     * Manual override mode for operator controller
     */
    private void climbMode() {
        // Set Lift arm position constant
        final double liftArmRotations = 65.625;

        // Set Climber winch position constant
        final double climberWinchRotations = 24;

        // Activate Lift based on joystick and constants multiplyer
        this.climber.setRobotArmPosition(this.operatorController.getJoystick(Side.LEFT, Axis.Y) * liftArmRotations);

        // Activate Climber based on joystick and constants multiplyer
        this.climber.setClimberWinchPosition(
                this.operatorController.getJoystick(Side.RIGHT, Axis.Y) * climberWinchRotations);
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
        this.climber.disable();
    }
}