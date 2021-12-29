package frc.io;

import frc.util.Controller;

public class DriverInput {
    private static DriverInput instance;

    private Controller driver;
    private Controller operator;

    /**
     * Get the instance of the DriverInput, if none create a new instance
     * 
     * @return instance of the DriverInput
     */
    public static DriverInput getInstance() {
        if (instance == null) {
            instance = new DriverInput();
        }
        return instance;
    }

    private DriverInput() {
        this.driver = new Controller(0);
        this.operator = new Controller(1);
    }

    /**
     * Get the driver controller
     * 
     * @return Driver controller
     */
    public Controller getDriverController() {
        return this.driver;
    }

    /**
     * Get the operator controller
     * 
     * @return Operator Controller
     */
    public Controller getOperatorController() {
        return this.operator;
    }

    /**
     * If either the driver or the operator presses the 'Y' button during autonomous, then it will cancel.
     * Note: This cannot be used during compilation but is useful for debugging code.
     * 
     * @return true if cancel button was pressed
     */
    public boolean getAutoOverride() {
        return this.driver.getYButton() || this.operator.getYButton();
    }
}
