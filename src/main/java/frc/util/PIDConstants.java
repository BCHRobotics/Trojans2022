package frc.util;

public class PIDConstants {
    public final double p;
	public final double i;
	public final double d;
	public final double eps;
	public final double ff;

    public PIDConstants(double pVal, double iVal, double dVal, double epsilon) {
		this(pVal, iVal, dVal, 0, epsilon);
	}

	public PIDConstants(double pVal, double iVal, double dVal, double ffVal, double epsilon) {
		this.p = pVal;
		this.i = iVal;
		this.d = dVal;
		this.ff = ffVal;
		this.eps = epsilon;
    }
}
