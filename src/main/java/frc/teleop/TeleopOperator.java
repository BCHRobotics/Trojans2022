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

    private enum OperatorMode {
        AUTOMATIC, OVERRIDE
    }

    private OperatorMode operatorMode = OperatorMode.OVERRIDE;

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
            case AUTOMATIC:
                auotmaticMode();
                break;
            case OVERRIDE:
                manualMode();
                break;
            default:
                auotmaticMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            if (operatorMode == OperatorMode.AUTOMATIC)
                operatorMode = OperatorMode.OVERRIDE;
            if (operatorMode == OperatorMode.OVERRIDE)
                operatorMode = OperatorMode.AUTOMATIC;
        }

        this.drive.calculate();
        this.shooter.calculate();
        this.climber.calculate();
    }

    private void auotmaticMode() {
        double distance = this.limelight.getTargetDistance();
        double height = Constants.TARGET_HEIGHT - Constants.SHOOTER_HEIGHT;

        trajectoy.setDistance(distance);
        trajectoy.setHeight(height);

        double angle = trajectoy.getAngle();
        double velocity = trajectoy.getVelocity();

        double shooterWheelRPM = (velocity*60)/(Math.PI*0.1016);
        double climberArmRevolutions = (angle/360) * Constants.liftArmGearReduction;

        if (operatorController.getYButton()) {
            climber.setRobotArmPosition(climberArmRevolutions);
            shooter.setShooterWheelSpeed(shooterWheelRPM);
        } else {
            climber.setRobotArmPosition(65);
            shooter.setShooterWheelSpeed(1200);
        }

        double kP = -0.1;
        double seekError = limelight.getTargetX() * kP;

        if (operatorController.getAButton()) {
            drive.setOutput(0, seekError);
        }
    }

    /**
     * Manual override mode for operator controller
     */
    private void manualMode() {
        // Set shooter wheel speed constant
        final double shooterWheelSpeed = 6000;

        // Set Lift arm position constant
        final double liftArmRotations = 65.625;
        
        // Set Climber winch position constant
        final double climberWinchRotations = 24;

        // Activate Shooter based on Y button and constants multiplyer
        if (operatorController.getYButton()) {
            shooter.setShooterWheelSpeed(shooterWheelSpeed);
        }

        // Activate Lift based on joystick and constants multiplyer
        climber.setRobotArmPosition(operatorController.getJoystick(Side.LEFT, Axis.Y) * liftArmRotations);

        // Activate Climber based on joystick and constants multiplyer
        climber.setClimberWinchPosition(operatorController.getJoystick(Side.RIGHT, Axis.Y) * climberWinchRotations);
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
        this.climber.disable();
    }
}