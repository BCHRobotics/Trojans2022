package frc.teleop;

import frc.io.DriverInput;
import frc.subsystems.Drive;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopDriver extends TeleopComponent {
    private static TeleopDriver instance;

    private Controller driverController;
    private DriverInput driverInput;

    private Drive drive;

     /**
     * Get the instance of the TeleopDriver, if none create a new instance
     * 
     * @return instance of the TeleopDriver
     */
    public static TeleopDriver getInstance() {
        if (instance == null) {
            instance = new TeleopDriver();
        }
        return instance;
    }

    private TeleopDriver() {
        this.driverInput = DriverInput.getInstance();
        this.drive = Drive.getInstance();
        this.driverController = driverInput.getDriverController();
    }


    @Override
    public void firstCycle() {
        this.drive.firstCycle();
    }

    @Override
    public void calculate() {
        
        double speed = 0.3;

        if (driverController.getLeftBumper()) speed = 0.5;
        if (driverController.getRightBumper()) speed = 1.0;

        drive.setOutput(
            driverController.getJoystick(Side.LEFT, Axis.Y) * speed, 
            driverController.getJoystick(Side.RIGHT, Axis.X) * speed
        );

        this.drive.calculate();
        
    }

    @Override
    public void disable() {
        this.drive.disable();
    }

}
