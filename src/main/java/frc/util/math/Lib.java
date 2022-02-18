package frc.util.math;

import frc.util.pid.Point;

public class Lib {
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

    public static double limitValue(double val) {
		return Lib.limitValue(val, 1.0);
	}

	public static double limitValue(double val, double max) {
		if (val > max) {
			return max;
		} else if (val < -max) {
			return -max;
		} else {
			return val;
		}
	}

	public static double limitValue(double val, double max, double min) {
		if (val > max) {
			return max;
		} else if (val < min) {
			return min;
		} else {
			return val;
		}
	}

	public static double squareMaintainSign(double val) {
		double output = val * val;

		// was originally negative
		if (val < 0) {
			output = -output;
		}

		return output;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Point turnCheck(Point p) {

		double a = p.getX();
		double b = p.getY();
		if (Math.abs(a) > Math.abs(b)) {
			if (Math.abs(a) > 1) {
				a /= Math.abs(a);
				b /= Math.abs(a);
			}
		} else {
			if (Math.abs(b) > 1) {
				a /= Math.abs(b);
				b /= Math.abs(b);
			}
		}
		return new Point(a, b);
	}

	public static double calcLeftTankDrive(double x, double y) {

		return (y + x);
	}

	public static double calcRightTankDrive(double x, double y) {

		return (y - x);
	}

	public static double max(double a, double b, double c) {
		a = Math.abs(a);
		b = Math.abs(b);
		c = Math.abs(c);
		if (a > b && a > c) {
			return a;
		} else if (b > c) {
			return b;
		} else {
			return c;
		}
	}

	public static boolean allWithinRange(double[] values, double target, double eps) {
		boolean withinRange = true;

		for (int i = 0; i < values.length; i++) {
			withinRange &= isWithinRange(target, values[i], eps);
		}

		return withinRange;

	}

	public static boolean isWithinRange(double target, double val, double eps) {
		if (Math.abs(val - target) <= eps) {
			return true;
		} else {
			return false;
		}
	}
}
