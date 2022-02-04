package frc.util.pid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDF extends PID {
    
	private double feedForward;

	public PIDF(double p, double i, double d, double f, double eps) {
		super(p, i, d, eps);
		this.feedForward = f;
	}

    public PIDF(PIDConstants constants) {
        super(
            constants.p,
            constants.i,
            constants.d,
            constants.eps
            );
        this.feedForward = constants.ff;
    }

	@Override
	public double calcPID(double current) {
		double feedForwardOutput = (super.getDesiredVal() * this.feedForward);
		if (this.debug) {
			SmartDashboard.putNumber("FF out", feedForwardOutput);
		}
		return super.calcPID(current) + feedForwardOutput;
	}

	public void setConstants(double p, double i, double d, double f) {
		super.setConstants(p, i, d);
		this.feedForward = f;
	}

	@Override
	public boolean isDone() {
		double currError = Math.abs(this.previousError);

		// close enough to target
		if (currError <= this.finishedRange) {
			return true;
		} else {
			return false;
		}
	}

	public void setFeedForward(double f) {
		this.feedForward = f;
	}

}
