package frc.imaging;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
        POWER_PORT, LOADING_BAY
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

        if (currentTarget == LimelightTargetType.POWER_PORT) {

            desiredTarget.setX(getVisionTargetDistance());

            desiredTarget.setY(getVisionTargetDistance());
            
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
     * Get the distance to the target (untested from 1114)
     * @return distance to target
     */
    public double getVisionTargetDistance() { 
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
