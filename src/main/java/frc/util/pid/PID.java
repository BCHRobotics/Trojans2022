package frc.util.pid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.util.math.Lib;

public class PID {
    private double pConst;
	private double iConst;
	private double dConst;
	private double desiredVal;
	protected double previousError;
	private double errorSum;
	protected double finishedRange;
	private double maxOutput;
	private double minOuput;
	private int minCycleCount;
	private int currentCycleCount;
	private boolean firstCycle;
	private boolean resetI;
	protected boolean debug;
	private double lastTime;
	private double deltaTime;
	private double iRange;

	public PID(PIDConstants constants) {
		this(constants.p, constants.i, constants.d, constants.eps);
	}

	public PID(double p, double i, double d, double epsRange) {
		this.pConst = p;
		this.iConst = i;
		this.dConst = d;
		this.finishedRange = epsRange;
		this.resetI = true;
		this.desiredVal = 0.0;
		this.firstCycle = true;
		this.maxOutput = 1.0;
		this.currentCycleCount = 0;
		this.minCycleCount = 5;
		this.debug = false;
		this.minOuput = 0;
		this.iRange = 1000; // Default high number to always apply I
	}

	public void setConstants(PIDConstants constants) {
		this.setConstants(constants.p, constants.i, constants.d);
		this.setFinishedRange(constants.eps);
	}

	public void setConstants(double p, double i, double d) {
		this.pConst = p;
		this.iConst = i;
		this.dConst = d;
	}

	public void setDesiredValue(double val) {
		this.desiredVal = val;
	}

	public void setFinishedRange(double range) {
		this.finishedRange = range;
	}

	public void disableIReset() {
		this.resetI = false;
	}

	public void enableIReset() {
		this.resetI = true;
	}

	public void enableDebug() {
		this.debug = true;
	}

	public void disableDebug() {
		this.debug = false;
	}

	public void setMaxOutput(double max) {
		this.maxOutput = max;
	}

	public void setMinMaxOutput(double min, double max) {
		this.maxOutput = max;
		this.minOuput = min;
	}

	public void setMinDoneCycles(int num) {
		this.minCycleCount = num;
	}

	public void resetErrorSum() {
		this.errorSum = 0.0;
	}

	public double getDesiredVal() {
		return this.desiredVal;
	}

	public void setIRange(double iRange) {
		this.iRange = iRange;
	}

	public double getIRange() {
		return this.iRange;
	}

	public double calcPID(double current) {
		return calcPIDError(this.desiredVal - current);
	}

	public double calcPIDError(double error) {
		double pVal = 0.0;
		double iVal = 0.0;
		double dVal = 0.0;

		if (this.firstCycle) {
			this.previousError = error;
			this.firstCycle = false;
			this.lastTime = System.currentTimeMillis();
			this.deltaTime = 20.0;
		} else {
			double currentTime = System.currentTimeMillis();
			this.deltaTime = currentTime - lastTime;
			this.lastTime = currentTime;
		}

		this.deltaTime = (this.deltaTime / 20.0); // 20ms is normal and should be 1

		/////// P Calc///////
		pVal = this.pConst * error;

		/////// I Calc///////
		if (Math.abs(error) < Math.abs(this.iRange)) { // Within desired range for using I
			this.errorSum += error * this.deltaTime;
		} else {
			this.errorSum = 0.0;
		}
		iVal = this.iConst * this.errorSum;


		/////// D Calc///////
		double deriv = (error - this.previousError) / this.deltaTime;
		dVal = this.dConst * deriv;

		// overal PID calc
		double output = pVal + iVal + dVal;

		// limit the output
		output = Lib.limitValue(output, this.maxOutput);

		if (output > 0) {
			if (output < this.minOuput) {
				output = this.minOuput;
			}
		} else {
			if (output > -this.minOuput) {
				output = -this.minOuput;
			}
		}

		// store current value as previous for next cycle
		this.previousError = error;

		if (this.debug) {
			SmartDashboard.putNumber("P out", pVal);
			SmartDashboard.putNumber("I out", iVal);
			SmartDashboard.putNumber("D out", dVal);
			SmartDashboard.putNumber("PID OutPut", output);
			SmartDashboard.putNumber("derivative", deriv);
			SmartDashboard.putNumber("error", error);
			SmartDashboard.putNumber("prev error", previousError);

		}

		return output;
	}

	public boolean isDone() {
		double currError = Math.abs(this.previousError);

		// close enough to target
		if (currError <= this.finishedRange) {
			this.currentCycleCount++;
		}
		// not close enough to target
		else {
			this.currentCycleCount = 0;
		}

		return this.currentCycleCount > this.minCycleCount;
	}

	public boolean getFirstCycle() {
		return this.firstCycle;
	}

	public void resetPreviousVal() {
		this.firstCycle = true;
	}
}
