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

    private double posLeft;
    private double posRight;

    private boolean positionMode;
    private boolean brakeMode;

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
        this.resetPosition();
    }

    @Override
    public void run() {
        SmartDashboard.putString("DRIVE_STATE", this.currentState.toString());

        if (this.positionMode) {
            this.currentState = DriveState.POSITION;
        } else {
            this.currentState = DriveState.OUTPUT;
        }

        switch (currentState) {
            case OUTPUT:
                this.driveIO.setDriveLeft(this.leftOut);
                this.driveIO.setDriveRight(this.rightOut);
                break;
            case VELOCITY:
                this.disable();
                break;
            case POSITION:
                this.driveIO.setDriveLeftPos(this.posLeft);
                this.driveIO.setDriveRightPos(this.posRight);
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
     * Reset encoders to zero position
     */
    public void resetPosition() {
        this.driveIO.resetInputs();
    }

    /**
     * Set drive position mode
     * @param state
     */
    public void setPositionMode(boolean state) {
        this.positionMode = state;
    }

    /**
     * Get drive position mode
     * @return
     */
    public boolean getPositionMode() {
        return this.positionMode;
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

    /**
     * Sets encoder position for left drive motors
     * @param position
     */
    public void setDriveLeft(double position) {
        this.posLeft = position;
    }

    /**
     * Sets encoder position for right drive motors
     * @param position
     */
    public void setDriveRight(double position) {
        this.posRight = position;
    }

    /**
     * Sets braking mode on drive motors
     * @param state
     */
    public void brake(boolean state) {
        this.brakeMode = state;
        this.driveIO.brakeMode(state);
    }

    /**
     * @return Drive motor brake state
     */
    public boolean getBrakeState() {
        return this.brakeMode;
    }

    /**
     * Turns drivetrain/chasis by a provided angle
     * @param angle
     */
    public void seekTarget(double angle) {
        SmartDashboard.putNumber("Limelight X", angle);

        double driveRevolutionsLeft = (angle / Constants.CHASIS_LEFT_CONVERSION);
        double driveRevolutionsRight = (angle / Constants.CHASIS_RIGHT_CONVERSION);

        this.resetPosition();
        this.setDriveLeft(driveRevolutionsLeft);
        this.setDriveRight(driveRevolutionsRight);
    }
}
