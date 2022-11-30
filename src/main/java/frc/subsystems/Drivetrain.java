package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.subsystems.DriveIO;
import frc.robot.Constants;

public class Drivetrain extends Subsystem {
    private static Drivetrain instance;

    public enum DriveState {
        OUTPUT, 
        VELOCITY,
        POSITION
    }

    private DriveIO driveIO;

    // states
    private DriveState currentState = DriveState.POSITION;

    private double leftOut;
    private double rightOut;

    /**
     * Get the instance of the Drive, if none create a new instance
     * 
     * @return instance of the Drive
     */
    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    private Drivetrain() {
        this.firstCycle();
    }

    @Override
    public void firstCycle() {
        this.driveIO = DriveIO.getInstance();
    }

    @Override
    public void run() {
        SmartDashboard.putString("DRIVE_STATE", this.currentState.toString());
        this.currentState = DriveState.OUTPUT;

        switch (currentState) {
            case OUTPUT:
                this.driveIO.setDriveLeft(this.leftOut);
                this.driveIO.setDriveRight(this.rightOut);
                break;
            case VELOCITY:
                this.disable();
                break;
            case POSITION:
                this.disable();
                break;
            default:
                this.disable();
                break;
        }
    }

    @Override
    public void disable() {
        this.driveIO.stopAllOutputs();
    }

    /**
     * Sets output to drive
     * @param y percent output [-1 to 1] for forward movement
     * @param turn percent output [-1 to 1] for turn movement
     */
    public void setOutput(double y, double turn) {
        this.leftOut = (y + turn) * Constants.MAX_OUTPUT;
        this.rightOut =  (y - turn) * Constants.MAX_OUTPUT;
    }
}
