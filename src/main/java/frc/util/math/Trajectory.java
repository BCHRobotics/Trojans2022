package frc.util.math;


public class Trajectory {
    
    // Create variables to store input data
    private double distance;
    private double height;
    
    // Set input distance
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    // Set input Height
    public void setHeight(double height) {
        this.height = height;
    }
    
    // Return angle after calculations
    public double getAngle() {
        return calculateAngle(this.distance, this.height);
    }
    
    // Reutrn velocity after calculations
    public double getVelocity() {
        return calculateVelocity(this.distance, this.height);
    }
    
    //call setShootingAngle to calculate for the nessessary shooter angle
    private double calculateAngle(double distance, double height){

        //calculate theoretical distance using given distance
        double theoDistanceC1 = Math.pow(distance,2)*0.0378;
        double theoDistanceC2 = 1.15*distance + theoDistanceC1 ;
        double theoDistance = theoDistanceC2 + 0.0397;

        //calculate S value using theoretical distance
        double sValue = (0.0679*Math.pow(theoDistance,2))+(2.38*theoDistance)-89.6;

        //calculate Shooting angle in degrees using theoretical Distance, height and Svalue
        double angleC1 = Math.tan(Math.toRadians(sValue))* theoDistance - (2*height);
        double angleC2 = angleC1/(-1*theoDistance);
        double angle = Math.atan((angleC2));
        return Math.toDegrees(angle);

    }

    //call setVelocityShooter to calculate he nessessary ball exit velocity using limelight distance and height
    private double calculateVelocity(double distance, double height){

        //calculate for theoretical distance with limelight distance
        double theoDistanceC1 = Math.pow(distance,2)*0.0378;
        double theoDistanceC2 = 1.15*distance + theoDistanceC1 ;
        double theoDistance = theoDistanceC2 + 0.0397;

        //calculate S value using theoretical distance
        double sValue = (0.0679*Math.pow(theoDistance,2))+(2.38*theoDistance)-89.6;

        //calculate Shooting angle in degrees using theoretical Distance, height and Svalue
        double angleC1 = Math.tan(Math.toRadians(sValue))* theoDistance - (2*height);
        double angleC2 = angleC1/(-1*theoDistance);
        double angle = Math.toDegrees(Math.atan((angleC2)));

        //calculate for exit velocity using theoDistance height and angle
        double velocityC1 = -9.81*Math.pow(theoDistance,2);
        double velocityC2 = (Math.pow(Math.tan(Math.toRadians(angle)),2)+ 1) * velocityC1;
        double velocityC3 = (2*height - 2* theoDistance * Math.tan(Math.toRadians(angle)));
        double velocity = velocityC2/velocityC3;

        return Math.sqrt(velocity);
        
    }
    
}
