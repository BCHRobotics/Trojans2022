package frc.util;

public class Lib {
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
