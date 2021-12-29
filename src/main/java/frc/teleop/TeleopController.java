package frc.teleop;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.io.DriverInput;
import frc.subsystems.Drive;
import frc.util.Controller;
import frc.util.Controller.Axis;
import frc.util.Controller.Side;

public class TeleopController extends TeleopComponent {
    private static TeleopController instance;

    private Controller driverController;
    private Controller operatorController;

    private DriverInput driverInput;

    public enum OperatorMode {
        DRIVE, CLIMB
    }

    private OperatorMode operatorMode = OperatorMode.DRIVE;

    private Drive drive;

     /**
     * Get the instance of the TeleopController, if none create a new instance
     * 
     * @return instance of the TeleopController
     */
    public static TeleopController getInstance() {
        if (instance == null) {
            instance = new TeleopController();
        }
        return instance;
    }

    private TeleopController() {
        this.driverInput = DriverInput.getInstance();

        this.drive = Drive.getInstance();

        this.driverController = driverInput.getDriverController();
        this.operatorController = driverInput.getOperatorController();
    }


    @Override
    public void firstCycle() {
        this.drive.firstCycle();
    }

    @Override
    public void calculate() {
        
        driver();

        switch (operatorMode) {
            case CLIMB:
                operatorClimb();
                break;
            case DRIVE:
            default:
                operatorDrive();
                break;
        }
        
    }

    @Override
    public void disable() {
        this.drive.disable();
    }

    /**
     * Driver controls
     */
    private void driver() {

        double speed = 0.75;

        if (driverController.getBumper(Hand.kLeft)) speed = 0.5;
        if (driverController.getBumper(Hand.kRight)) speed = 1.0;

        drive.setOutput(
            driverController.getJoystick(Side.LEFT, Axis.Y) * speed, 
            driverController.getJoystick(Side.RIGHT, Axis.X) * speed
        );

    }

    /**
     * Operator drive controls
     */
    private void operatorDrive() {

    }
    
    /**
     * Operator climb controls
     */
    private void operatorClimb() {

    }

}
