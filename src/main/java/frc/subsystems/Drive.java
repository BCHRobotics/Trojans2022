package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.subsystems.DriveIO;
import frc.io.subsystems.IO;
import frc.robot.Constants;
import frc.util.math.Lib;

public class Drive extends Subsystem {
    private static Drive instance;

    public enum DriveState {
        OUTPUT, 
        VELOCITY,
        POSITION
    }

    private DriveIO driveIO;

    // states
    private DriveState currentState = DriveState.OUTPUT;
    private double leftOut;
    private double rightOut;

    /**
     * Get the instance of the Drive, if none create a new instance
     * 
     * @return instance of the Drive
     */
    public static Drive getInstance() {
        if (instance == null) {
            instance = new Drive();
        }
        return instance;
    }

    private Drive() {
        this.driveIO = DriveIO.getInstance();

        this.firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        SmartDashboard.putString("DRIVE_STATE", this.currentState.toString());

        switch (currentState) {
            case OUTPUT:
            case VELOCITY:
                this.driveIO.setDriveLeft(this.leftOut);
                this.driveIO.setDriveRight(this.rightOut);
                break;
            default:
                disable();
                break;
        }
    }

    @Override
    public void disable() {
        this.driveIO.setDriveLeft(0.0);
        this.driveIO.setDriveRight(0.0);
    }

    /**
     * Sets output to drive
     * @param y percent output [-1 to 1] for forward movement
     * @param turn percent output [-1 to 1] for turn movement
     */
    public void setOutput(double y, double turn) {
        this.currentState = DriveState.OUTPUT;

        this.leftOut = (y + turn) * Constants.MAX_OUTPUT;
        this.rightOut =  (y - turn) * Constants.MAX_OUTPUT;
    }
}
