package frc.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.DriverInput;
import frc.subsystems.Drive;
import frc.subsystems.Intake;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopDriver extends TeleopComponent {
    private static TeleopDriver instance;

    private Controller driverController;
    private DriverInput driverInput;

    private Drive drive;
    private Intake intake;

    private boolean intakeToggle = false;

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
        this.intake = Intake.getInstance();
        this.driverController = driverInput.getDriverController();
    }


    @Override
    public void firstCycle() {
        this.drive.firstCycle();
        this.intake.firstCycle();
        SmartDashboard.putNumber("Intake Motor Speed", 0);
    }

    @Override
    public void calculate() {
        
        double speed = 0.65;

        this.drive.setOutput(
            driverController.getJoystick(Side.LEFT, Axis.Y) * speed, 
            driverController.getJoystick(Side.RIGHT, Axis.X) * speed
        );

        if (driverController.getRightBumper()) {
            this.intake.setIntakeState(this.intakeToggle);
            this.intakeToggle = !this.intakeToggle;
        }

        if (driverController.getAButton()) {
            intakeControl(SmartDashboard.getNumber("Intake Motor Speed",0));
        } else {
            intakeControl(0);
        }

        this.drive.calculate();
        this.intake.calculate();
    }

    public void intakeControl(double speed) {
        this.intake.setStagerSpeed(speed);
        this.intake.setIntakeSpeed(speed);
    }

    @Override
    public void disable() {
        this.drive.disable();
    }

}
