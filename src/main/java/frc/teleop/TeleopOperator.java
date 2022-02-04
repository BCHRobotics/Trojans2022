package frc.teleop;

import frc.io.DriverInput;
import frc.subsystems.Climber;
import frc.subsystems.Shooter;
import frc.util.Controller;
import frc.util.Controller.Axis;
import frc.util.Controller.Side;

public class TeleopOperator extends TeleopComponent {
    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private enum OperatorMode {
        CLIMB, DRIVE, SHOOT
    }

    private OperatorMode operatorMode = OperatorMode.CLIMB;

    private Climber climber;
    private Shooter shooter;

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

    @Override
    public void firstCycle() {
        this.driverInput = DriverInput.getInstance();
        this.operatorController = driverInput.getOperatorController();
        this.climber = Climber.getInstance();
        this.shooter = Shooter.getInstance();
    }

    @Override
    public void calculate() {

        System.out.println("Teleop Operator Calculate!");

        switch (operatorMode) {
            case CLIMB:
                climbMode();
                break;
            case SHOOT:
                shootMode();
                break;
            case DRIVE:
            default:
                driveMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            if (operatorMode == OperatorMode.CLIMB) operatorMode = OperatorMode.DRIVE;
            if (operatorMode == OperatorMode.DRIVE) operatorMode = OperatorMode.SHOOT;
            if (operatorMode == OperatorMode.SHOOT) operatorMode = OperatorMode.CLIMB;
        }

        shooter.calculate();
        climber.calculate();
    }

    /**
     * Climb mode for operator controller
     */
    private void climbMode() {

        System.out.println("Climb Mode!");

        double speed = 0.3;
        
        climber.setSyncExtendOutput(
            operatorController.getJoystick(Side.LEFT, Axis.Y) * speed
        );

        climber.setSyncRotateOutput(
            operatorController.getJoystick(Side.RIGHT, Axis.X) * speed
        );
    }

    /**
     * Climb mode for operator controller
     */
    private void shootMode() {

        System.out.println("Shoot Mode!");

        double speed = 0.3;
        double position = 80;
        
        shooter.setShooterWheelSpeed(
            operatorController.getJoystick(Side.LEFT, Axis.Y) * speed
        );

        shooter.setShooterTurretPosition(
            operatorController.getJoystick(Side.RIGHT, Axis.X) * position
        );
    }

    /**
     * Drive mode for operator controller
     */
    private void driveMode() {

    }

    @Override
    public void disable() {
        
    }
    
}
