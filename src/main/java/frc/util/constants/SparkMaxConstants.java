package frc.util.constants;

public class SparkMaxConstants {
    //#region turret

        // PID coefficients
        public final double kP; 
        public final double kI;
        public final double kD; 
        public final double kIz; 
        public final double kFF;
        public final double kMinOutput;
        public final double kMaxOutput;

        // Smart Motion Coefficients
        public final int slot;
        public final double minVel; // rpm
        public final double maxVel; // rpm
        public final double maxAcc; // rpm
        public final double allowedErr;

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
}
