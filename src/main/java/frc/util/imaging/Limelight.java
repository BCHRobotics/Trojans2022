package frc.util.imaging;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class Limelight {
    private static Limelight instance;

    private NetworkTable networkTable;
    private LimelightTargetType currentTarget;

    public static Limelight getInstance() {
        if (instance == null) {
            instance = new Limelight();
        }
        return instance;
    }

    public enum LimelightTargetType {
        UPPER_HUB, LOADING_BAY, POWER_PORT
    }

    public Limelight() {
        this.networkTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public void setLimelightState(LimelightTargetType state) {
        this.currentTarget = state;
    }

    public LimelightTargetType getLimelightState() {
        return this.currentTarget;
    }

    public LimelightTarget getTargetInfo() {

        LimelightTarget desiredTarget = new LimelightTarget(this.currentTarget);
        SmartDashboard.putString("LIMELIGHT_TARGET", this.currentTarget.toString());

        if (currentTarget == LimelightTargetType.UPPER_HUB) {

            desiredTarget.setX(getTargetDistance());

            desiredTarget.setY(getTargetDistance());
            
            desiredTarget.setTargetFound(getTargetExists());

        } else {
            desiredTarget.setTargetFound(false);
        }

        return desiredTarget;
    }

    /**
     * Change the mode of the led 1-off, 2-blink, 3-on
     * @param ledMode led mode
     */
    public void setLedMode(int ledMode) {
        this.networkTable.getEntry("ledMode").setNumber(ledMode);
    }

    public double getTargetX() {
        return this.networkTable.getEntry("tx").getDouble(0);
    }

    public double getTargetY() {
        return this.networkTable.getEntry("ty").getDouble(0);
    }

    public boolean getTargetExists() {
        return this.networkTable.getEntry("tv").getDouble(0) == 1;
    }

    public double getTargetArea() {
        return this.networkTable.getEntry("ta").getDouble(0);
    }

    /**
     * Get the distance to the target by using SOH CAH TOA
     * @return distance to target
     */
    public double getTargetDistance() { 
        double a1 = Constants.LIMELIGHT_ANGLE; // Limelight mount angle
        double a2 = this.getTargetY(); // Limelight measured angle to target
        double aR = (a1+a2) * (Math.PI/180); // Total anlge in Radians
        double h1 = Constants.LIMELIGHT_HEIGHT; // Limelight lens Height;
        double h2 = Constants.TARGET_HEIGHT; // Known Height of Target

        double distance = (h2-h1)/Math.tan(aR);
        
        SmartDashboard.putNumber("Distance", distance);
        return distance;
    }

    /**
     * Get the distance to the target (untested from 1114) by using Target Area
     * @return distance to target
     */
    public double getTargetDistanceFromArea() { 
        double x = this.getTargetArea();
        double distance;
        if (x == 0 || x > 12.0) {
            distance = 2.3 * 12.0;
        } else {
            distance = Math.pow(x, -0.507) * 7.7562;
        }
        SmartDashboard.putNumber("Distance", distance);
        return distance;
    }
}
