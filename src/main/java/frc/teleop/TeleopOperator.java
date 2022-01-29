package frc.teleop;

import frc.io.DriverInput;
import frc.subsystems.Climber;
import frc.util.Controller;
import frc.util.Controller.Axis;
import frc.util.Controller.Side;

public class TeleopOperator extends TeleopComponent {
    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private enum OperatorMode {
        CLIMB, DRIVE
    }

    private OperatorMode operatorMode = OperatorMode.DRIVE;

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

    @Override
    public void firstCycle() {
        this.driverInput = DriverInput.getInstance();
        this.operatorController = driverInput.getOperatorController();
        this.climber = Climber.getInstance();
    }

    @Override
    public void calculate() {
        switch (operatorMode) {
            case CLIMB:
                climbMode();
                break;
            case DRIVE:
            default:
                driveMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            if (operatorMode == OperatorMode.CLIMB) operatorMode = OperatorMode.DRIVE;
            if (operatorMode == OperatorMode.DRIVE) operatorMode = OperatorMode.CLIMB;
        }

        climber.calculate();
    }

    /**
     * Climb mode for operator controller
     */
    private void climbMode() {
        double speed = 0.3;
        
        climber.setSyncExtendOutput(
            operatorController.getJoystick(Side.LEFT, Axis.Y) * speed
        );

        climber.setSyncRotateOutput(
            operatorController.getJoystick(Side.RIGHT, Axis.X) * speed
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
