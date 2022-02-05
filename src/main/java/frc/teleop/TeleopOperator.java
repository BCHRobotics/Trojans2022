package frc.teleop;

import frc.io.DriverInput;
import frc.subsystems.Shooter;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopOperator extends TeleopComponent {
    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private enum OperatorMode {
        DRIVE, SHOOT
    }

    private OperatorMode operatorMode = OperatorMode.SHOOT;

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
        this.shooter = Shooter.getInstance();
    }

    @Override
    public void calculate() {

        System.out.println("Teleop Operator Calculate!");

        switch (operatorMode) {
            case SHOOT:
                shootMode();
                break;
            case DRIVE:
            default:
                driveMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            if (operatorMode == OperatorMode.DRIVE) operatorMode = OperatorMode.SHOOT;
            if (operatorMode == OperatorMode.SHOOT) operatorMode = OperatorMode.DRIVE;
        }

        shooter.calculate();
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
