package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.subsystems.DriveIO;
import frc.robot.Constants;

public class Drive extends Subsystem {
    private static Drive instance;

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

    private double posLeft;
    private double posRight;

    private boolean shooterLock;
    private boolean brakeLock;

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
        this.firstCycle();
    }

    @Override
    public void firstCycle() {
        this.driveIO = DriveIO.getInstance();
        this.resetPosition();
    }

    @Override
    public void calculate() {
        SmartDashboard.putString("DRIVE_STATE", this.currentState.toString());

        if (this.shooterLock) {
            this.currentState = DriveState.POSITION;
        }

        switch (currentState) {
            case OUTPUT:
                this.driveIO.setDriveLeft(this.leftOut);
                this.driveIO.setDriveRight(this.rightOut);
                break;
            case VELOCITY:
                disable();
                break;
            case POSITION:
                this.driveIO.setDriveLeftPos(this.posLeft);
                this.driveIO.setDriveRightPos(this.posRight);
                break;
            default:
                disable();
                break;
        }
    }

    @Override
    public void disable() {
        this.driveIO.stopAllOutputs();
    }

    public void resetPosition() {
        this.driveIO.resetInputs();
    }

    public void setLockPosition(boolean state) {
        this.shooterLock = state;
    }

    public boolean getLockPosition() {
        return this.shooterLock;
    }

    public void setBrakeMode(boolean state) {
        this.brakeLock = state;
    }

    public boolean getBrakeMode() {
        return this.brakeLock;
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

    public void setDriveLeft(double position) {
        this.currentState = DriveState.POSITION;
        this.posLeft = position;
    }

    public void setDriveRight(double position) {
        this.currentState = DriveState.POSITION;
        this.posRight = position;
    }

    public void brake(boolean mode) {
        this.driveIO.brakeMode(mode);
    }
}
