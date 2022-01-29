package frc.io;

import frc.util.Controller;
import frc.util.Controller.Axis;
import frc.util.Controller.Side;

public class DriverInput {
    private static DriverInput instance;

    private Controller driver;
    private Controller operator;

    // Creates boolean variables that stores if a certain step/mode was pressed
	private boolean autonIncreaseStepWasPressed = false;
	private boolean autonDecreaseStepWasPressed = false;

	private boolean autonIncreaseModeWasPressed = false;
	private boolean autonDecreaseModeWasPressed = false;

	private boolean autonIncreaseMode10WasPressed = false;
	private boolean autonDecreaseMode10WasPressed = false;

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

        // ********************************
	// AUTO SELECTION CONTROLS
	// ********************************

	public boolean getResumeAutoButton() {
		return driver.getYButton();
	}

	public boolean getAutonSetDelayButton() {
		return false;//this.driver.getRightTrigger() > 0.2;
	}

	public double getAutonDelayStick() {
		return this.driver.getJoystick(Side.LEFT, Axis.Y);
	}

	public boolean getAutonStepIncrease() {
		// only returns true on rising edge
		boolean result = this.driver.getRightBumper() && !this.autonIncreaseStepWasPressed;
		this.autonIncreaseStepWasPressed = this.driver.getRightBumper();
		return result;

	}

	public boolean getAutonStepDecrease() {
		// only returns true on rising edge
		boolean result = this.driver.getLeftBumper() && !this.autonDecreaseStepWasPressed;
		this.autonDecreaseStepWasPressed = this.driver.getLeftBumper();
		return result;

	}

	public boolean getAutonModeIncrease() {
		// only returns true on rising edge
		boolean result = this.driver.getXButton() && !this.autonIncreaseModeWasPressed;
		this.autonIncreaseModeWasPressed = this.driver.getXButton();
		return result;

	}

	public boolean getAutonModeDecrease() {
		// only returns true on rising edge
		boolean result = this.driver.getAButton() && !this.autonDecreaseModeWasPressed;
		this.autonDecreaseModeWasPressed = this.driver.getAButton();
		return result;

	}

	public boolean getAutonModeIncreaseBy10() {
		// only returns true on rising edge
		boolean result = this.driver.getYButton() && !this.autonIncreaseMode10WasPressed;
		this.autonIncreaseMode10WasPressed = this.driver.getYButton();
		return result;

	}

	public boolean getAutonModeDecreaseBy10() {
		// only returns true on rising edge
		boolean result = this.driver.getXButton() && !this.autonDecreaseMode10WasPressed;
		this.autonDecreaseMode10WasPressed = this.driver.getXButton();
		return result;

	}
}
