package frc.util;

public class Point {
    private double x;
	private double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public void rotateByAngleDegrees(double angle) {
		angle = Math.toRadians(angle);

		double x_ = x * Math.cos(angle) - y * Math.sin(angle);
		double y_ = x * Math.sin(angle) + y * Math.cos(angle);

		this.x = x_;
		this.y = y_;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
