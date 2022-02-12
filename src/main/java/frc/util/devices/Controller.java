package frc.util.devices;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;

public class Controller extends XboxController {

    public enum Side {
        LEFT, RIGHT
    }

    public enum Axis {
        X, Y
    }

    public Controller(int port) {
        super(port);
    }

    public boolean getModeSwitchButtonsPressed() {
        return this.getStartButtonPressed() || this.getBackButtonPressed();
    }

    public double getJoystick(Side side, Axis axis) {
        double deadzone = Constants.CONTROLLER_DEADZONE;

        boolean left = side == Side.LEFT;
        boolean y = axis == Axis.Y;
        // multiplies by -1 if y-axis (inverted normally)
        return handleDeadzone((y ? -1 : 1) * this.getRawAxis((left ? 0 : 4) + (y ? 1 : 0)), deadzone);
    }

    private double handleDeadzone(double value, double deadzone) {
        return (Math.abs(value) > Math.abs(deadzone)) ? value : 0;
    }
}
