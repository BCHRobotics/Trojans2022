package frc.util.pid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SparkMaxConstants {
    //#region turret

        // PID coefficients
        public double kP; 
        public double kI;
        public double kD; 
        public double kIz; 
        public double kFF;
        public double kMinOutput;
        public double kMaxOutput;

        // Smart Motion Coefficients
        public int slot;
        public double minVel; // rpm
        public double maxVel; // rpm
        public double maxAcc; // rpm
        public double allowedErr;

    ////#endregion turret

    /**
     * PID Values for Spark MAX Controller
     * @param kP
     * @param kI
     * @param kD
     * @param kIz
     * @param kFF
     * @param kMinOutput
     * @param kMaxOutput
     * @param slot
     * @param minVel
     * @param maxVel
     * @param maxAcc
     * @param allowedErr
     */
    public SparkMaxConstants(double kP, double kI, double kD, double kIz, double kFF, double kMinOutput, double kMaxOutput, int slot, double minVel, double maxVel, double maxAcc, double allowedErr) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kIz = kIz;
        this.kFF = kFF;
        this.kMinOutput = kMinOutput;
        this.kMaxOutput = kMaxOutput;
        this.minVel = minVel;
        this.maxVel = maxVel;
        this.maxAcc = maxAcc;
        this.allowedErr = allowedErr;
        this.slot = slot;
    }

    public void pushToDashboard(String name) {
        SmartDashboard.putNumber(name + " P Gain", kP);
        SmartDashboard.putNumber(name + " I Gain", kI);
        SmartDashboard.putNumber(name + " D Gain", kD);
        SmartDashboard.putNumber(name + " I Zone", kIz);
        SmartDashboard.putNumber(name + " Feed Forward", kFF);
        SmartDashboard.putNumber(name + " Max Output", kMaxOutput);
        SmartDashboard.putNumber(name + " Min Output", kMinOutput);

        SmartDashboard.putNumber(name + " Max Velocity", maxVel);
        SmartDashboard.putNumber(name + " Min Velocity", minVel);
        SmartDashboard.putNumber(name + " Max Acceleration", maxAcc);
        SmartDashboard.putNumber(name + " Allowed Closed Loop Error", allowedErr);
    }

    public void getFromDashboard(String name) {
        kP = SmartDashboard.getNumber(name + " P Gain", 0);
        kI = SmartDashboard.getNumber(name + " I Gain", 0);
        kD = SmartDashboard.getNumber(name + " D Gain", 0);
        kIz = SmartDashboard.getNumber(name + " I Zone", 0);
        kFF = SmartDashboard.getNumber(name + " Feed Forward", 0);
        kMaxOutput = SmartDashboard.getNumber(name + " Max Output", 0);
        kMinOutput = SmartDashboard.getNumber(name + " Min Output", 0);

        maxVel = SmartDashboard.getNumber(name + " Max Velocity", 0);
        minVel = SmartDashboard.getNumber(name + " Min Velocity", 0);
        maxAcc = SmartDashboard.getNumber(name + " Max Acceleration", 0);
        allowedErr = SmartDashboard.getNumber(name + " Allowed Closed Loop Error", 0);
    }

    @Override
    public String toString() {
        return String.format("kP: %f, kI: %f, kD: %f, kIz: %f, kFF: %f\n" + 
            "kMaxOutput: %f, kMinOutput: %f\n" +
            "maxVel: %f, minVel: %f, maxAcc: %f, allowedErr: %f",
            kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxVel, minVel, maxAcc, allowedErr);
    }
}
