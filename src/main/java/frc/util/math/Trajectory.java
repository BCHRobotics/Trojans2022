package frc.util.math;

public class Trajectory {
    //call theoDistance with input distance to return distance w air resistance accounted
    public static double theoDistance(double distance){
        double c1 = Math.pow(distance,2)*0.0378;
        double c2 = 1.15*distance + c1 ;
        return c2 + 0.0397;
    }

	//call sValue with input distance (theoDist) to output angle of approach value 
    public static double sValue(double distance) {

        return (0.0679*Math.pow(distance,2))+(2.38*distance)-89.6;
    }

	// call angle with input theoDistance, height, and sValue to return the shooting angle
    public static double angle(double distance, double height, double sValue) {

        double t1 = Math.tan(Math.toRadians(sValue))* distance - (2*height);
        double t2 = t1/(-1*distance);
        double t3 = Math.atan((t2));
        return Math.toDegrees(t3);
    }  

    // call velocity with input theoDistance, shooting angle, and height to return exit velocity value
    public static double velocity(double distance, double angle, double height) {

        double t1 = -9.81*Math.pow(distance,2);
        double t12 = (Math.pow(Math.tan(Math.toRadians(angle)),2)+ 1) * t1;
        double t2 = (2*height - 2* distance * Math.tan(Math.toRadians(angle)));
        double t22 = t12/t2;
        return Math.sqrt(t22);
    }

    
    
}
