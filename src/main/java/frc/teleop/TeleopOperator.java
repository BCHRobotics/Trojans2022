package frc.teleop;

import frc.io.DriverInput;
import frc.util.devices.Controller;

public class TeleopOperator extends TeleopComponent {
    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private enum OperatorMode {
        CLIMB, DRIVE
    }

    private OperatorMode operatorMode = OperatorMode.DRIVE;

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
    }

    /**
     * Climb mode for operator controller
     */
    private void climbMode() {
        
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
