package frc.util.math;


public class Trajectory {
    
    private static double distance;
    private static double height;
    private static double angle;
    private static double velocity;
    
    public static void setDistance(double distance) {
        this.distance = distance;
    }
    
    public static void setHeight(double height) {
        this.height = height;
    }
    
    public static double getAngle() {
        return calculateAngle(this.distance, this.height);
    }
    
    public static double getVelocity() {
        return calculateVelocity(this.distance, this.height);
    }
    
    //call setShootingAngle to calculate for the nessessary shooter angle
    private static double calculateAngle(double distance, double height){

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
    private static double calculateVelocity(double distance, double height){

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
