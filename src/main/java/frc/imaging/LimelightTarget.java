package frc.imaging;

import frc.imaging.Limelight.LimelightTargetType;

public class LimelightTarget {
    
    private double x;
    private double y;
    private LimelightTargetType desiredTargetType;
    private boolean targetFound;

    public LimelightTarget(LimelightTargetType desiredTargetType) {
        this.desiredTargetType = desiredTargetType;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public LimelightTargetType getDesiredTargetType() {
        return desiredTargetType;
    }

    public void setDesiredTargetType(LimelightTargetType desiredTargetType) {
        this.desiredTargetType = desiredTargetType;
    }

    public boolean isTargetFound() {
        return targetFound;
    }

    public void setTargetFound(boolean targetFound) {
        this.targetFound = targetFound;
    }

}
