package frc.teleop;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.io.DriverInput;
import frc.subsystems.Drive;
import frc.util.Controller;
import frc.util.Controller.Axis;
import frc.util.Controller.Side;

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
        
        double speed = 0.75;

        if (driverController.getBumper(Hand.kLeft)) speed = 0.5;
        if (driverController.getBumper(Hand.kRight)) speed = 1.0;

        drive.setOutput(
            driverController.getJoystick(Side.LEFT, Axis.Y) * speed, 
            driverController.getJoystick(Side.RIGHT, Axis.X) * speed
        );
        
    }

    @Override
    public void disable() {
        this.drive.disable();
    }

}
