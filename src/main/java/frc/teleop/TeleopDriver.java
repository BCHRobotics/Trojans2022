package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.intake.Collect;
import frc.io.DriverInput;
import frc.subsystems.Drivetrain;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopDriver extends TeleopComponent {
    private static TeleopDriver instance;

    private Drivetrain drivetrain;
    private Collect collect;

    private Controller driverController;
    private DriverInput driverInput;

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
        this.drivetrain = Drivetrain.getInstance();
        this.collect = Collect.getInstance();
        this.driverController = driverInput.getDriverController();
    }

    @Override
    public void firstCycle() {
        this.drivetrain.firstCycle();
        this.collect.initialize();
        SmartDashboard.putNumber("Intake Motor Speed", 0.8);
    }

    @Override
    public void run() {

        double speed = 0.75;

        speed -= (this.driverController.getLeftTriggerAxis() * 0.25);
        speed += (this.driverController.getRightTriggerAxis() * 0.25);

        SmartDashboard.putNumber("Drive Output", speed);

        this.drivetrain.setOutput(
                driverController.getJoystick(Side.LEFT, Axis.Y) * speed,
                driverController.getJoystick(Side.RIGHT, Axis.X) * speed);

        if (!this.drivetrain.getBrakeState()) {
            if (driverController.getLeftBumper())
                this.drivetrain.brake(true);
            else
                this.drivetrain.brake(false);
        }

        if (driverController.getRightBumper())
            this.collect.calculate();
        else
            this.collect.end();

        this.drivetrain.run();
        this.collect.execute();
    }

    @Override
    public void disable() {
        this.drivetrain.disable();
        this.collect.disable();
    }
}
