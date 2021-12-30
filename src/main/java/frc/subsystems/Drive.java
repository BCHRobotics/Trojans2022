package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.Input.SensorInput;
import frc.io.Output.RobotOutput;
import frc.robot.Constants;
import frc.util.Lib;
import frc.util.PID;
import frc.util.PIDF;
import frc.util.Point;

public class Drive extends Subsystem {
    private static Drive instance;

    public enum DriveState {
        OUTPUT, 
        VELOCITY
    }

    private RobotOutput robotOutput;
    private SensorInput sensorInput;

    // states
    private DriveState currentState = DriveState.OUTPUT;
    private double leftOut;
    private double rightOut;

    // PID
    private PIDF leftVelPID;
    private PIDF rightVelPID;
    private PID straightPID;
    private PID turnPID;

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
        this.robotOutput = RobotOutput.getInstance();
        this.sensorInput = SensorInput.getInstance();

        this.firstCycle();
    }

    @Override
    public void firstCycle() {
        this.straightPID = new PID(Constants.getDriveStraightPID());
        this.straightPID.setMinDoneCycles(1);
        this.straightPID.setMinDoneCycles(10);
        this.straightPID.setIRange(1);

        this.leftVelPID = new PIDF(Constants.getDriveVelocityPID());
        this.leftVelPID.setMaxOutput(1);

        this.rightVelPID = new PIDF(Constants.getDriveVelocityPID());
        this.rightVelPID.setMaxOutput(1);

        this.turnPID = new PID(Constants.getDriveTurnPID());
        this.turnPID.setMinDoneCycles(10);
        this.turnPID.setMaxOutput(10);
        this.turnPID.setIRange(10);
    }

    @Override
    public void calculate() {
        SmartDashboard.putString("DRIVE_STATE", this.currentState.toString());

        switch (currentState) {
            case OUTPUT:
            case VELOCITY:
                this.robotOutput.drive.setDriveLeft(this.leftOut);
                this.robotOutput.drive.setDriveRight(this.rightOut);
                break;
            default:
                disable();
                break;
        }
    }

    @Override
    public void disable() {
        this.robotOutput.drive.setDriveLeft(0.0);
        this.robotOutput.drive.setDriveRight(0.0);
    }

    /**
     * Get the rotated error of the robot position uses {@link Point} to calculate
     * how far off the robot is from the desired X and Y
     * @param theta desired angle
     * @param desiredX desired X position 
     * @param desiredY desired Y position
     * @return {@link Point} with the x and y of the error
     * @see Point
     * @see SensorInput#getDriveXPos()
     * @see SensorInput#getDriveYPos()
     */
    private Point getRotatedError(double theta, double desiredX, double desiredY) {
        double currentX = this.sensorInput.drive.getDriveXPos();
        double currentY = this.sensorInput.drive.getDriveYPos();
        double rotation = 90 - theta;

        Point currentPosition = new Point(currentX, currentY);
        Point finalPosition = new Point(desiredX, desiredY);

        currentPosition.rotateByAngleDegrees(rotation);
        finalPosition.rotateByAngleDegrees(rotation);

        double xError = finalPosition.getX() - currentPosition.getX();
        double yError = finalPosition.getY() - currentPosition.getY();

        return new Point(xError, yError);
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

    /**
     * Set the target velocity of the Drive
     * @param targetVel target feet per second
     */
    public void setTargetVelocity(double targetVel) {
        this.leftVelPID.setDesiredValue(targetVel);
        this.rightVelPID.setDesiredValue(targetVel);
    }

    /**
     * Drive at velocity (FPS)
     * @param velocity target feet per second
     */
    public void driveAtVelocity(double velocity) {
        this.currentState = DriveState.VELOCITY;
        this.setTargetVelocity(velocity);
        double left = this.leftVelPID.calcPID(this.sensorInput.drive.getDriveSpeedFPS());
        double right = this.rightVelPID.calcPID(this.sensorInput.drive.getDriveSpeedFPS());
        this.robotOutput.drive.setDriveLeft(left);
        this.robotOutput.drive.setDriveRight(right);
    }

    /**
     * Set the velocity output per side of the Drive
     * @param leftOut target feet per second of left motors
     * @param rightOut target feet per second of right motors
     */
    public void setVelocityOutput(double leftOut, double rightOut) {
        this.currentState = DriveState.VELOCITY;

        this.leftVelPID.setDesiredValue(leftOut);
        this.rightVelPID.setDesiredValue(rightOut);

        rightOut = this.rightVelPID.calcPID(this.sensorInput.drive.getDriveRightSpeedFPS());
        leftOut = this.leftVelPID.calcPID(this.sensorInput.drive.getDriveLeftSpeedFPS());

        this.robotOutput.drive.setDriveLeft(leftOut);
        this.robotOutput.drive.setDriveRight(rightOut);
    }

    /**
     * Set the ramp rate for the Drive
     * @param rate ramp rate seconds to full
     */
    public void setRampRate(double rate) {
        this.robotOutput.drive.setDriveRampRate(rate);
    }

    /**
     * Drive the robot to a specific point.
     * 
     * @param x x position to move to
     * @param y y position to move to
     * @param theta angle to move to
     * @param minVelocity minimum velocity to move at
     * @param maxVelocity maximum velocity to move at
     * @param turnRate rate at the drive will turn
     * @param maxTurn max angle to turn
     * @param eps max error to finish at
     * @return true if successfully completed action
     * 
     * @see frc.auton.drive.DriveToPoint
     */
	public boolean DriveToPoint(double x, double y, double theta, double minVelocity, double maxVelocity,
        double turnRate, double maxTurn, double eps) {

        this.straightPID.setMinMaxOutput(minVelocity, maxVelocity);
        Point error = getRotatedError(theta, x, y);
        double targetHeading;
        this.straightPID.setFinishedRange(eps);
        this.turnPID.setMaxOutput(10);

        if (error.getY() < 0) { // flip X if we are going backwards
            error.setX(-error.getX());
        }

        double turningOffset = (error.getX() * turnRate); // based on how far we are in x turn more
        
        if (turningOffset > maxTurn) { 
            turningOffset = maxTurn;
        } else if (turningOffset < -maxTurn) {
            turningOffset = -maxTurn;
        }
        
        targetHeading = theta - turningOffset;

        double angle = sensorInput.drive.getGyroAngle();

        this.turnPID.setDesiredValue(targetHeading);

        

        double yError = error.getY();
        double yOutput;

        if (Math.abs(yError) > 3.0) {
            this.robotOutput.drive.setDriveRampRate(0.20);
        } else {
            this.robotOutput.drive.setDriveRampRate(0);
        }

        yOutput = this.straightPID.calcPIDError(yError);
        

        double distanceFromTargetHeading = Math.abs(this.turnPID.getDesiredVal() - angle);
        if (distanceFromTargetHeading > 90) { // prevents the y output from being reversed in the next calculation
            distanceFromTargetHeading = 90;
        }

        yOutput = yOutput * (((-1 * distanceFromTargetHeading) / 90.0) + 1);

        double xOutput = -this.turnPID.calcPID(angle);


        double leftOut = Lib.calcLeftTankDrive(xOutput, yOutput);
        double rightOut = Lib.calcRightTankDrive(xOutput, yOutput);

        this.setVelocityOutput(leftOut, rightOut);

        double dist = (yError);
        if (this.straightPID.isDone()) {
            System.out.println("I have reached the epsilon!");
        }

        boolean isDone = false;
        if (minVelocity <= 0.5) {
            if (this.straightPID.isDone()) {
                disable();
                isDone = true;
                this.robotOutput.drive.setDriveRampRate(0);
            }
        } else if (Math.abs(dist) < eps) {
            isDone = true;
            this.robotOutput.drive.setDriveRampRate(0);
        }

        return isDone;
    }
    
}
