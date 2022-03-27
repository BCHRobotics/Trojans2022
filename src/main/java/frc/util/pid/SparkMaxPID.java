package frc.util.pid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

public class SparkMaxPID {

    private SparkMaxPIDController pidController;

    private int slot;

    public SparkMaxPID(CANSparkMax motor) {
        pidController = motor.getPIDController();
    }

    public SparkMaxPID(CANSparkMax motor, SparkMaxConstants constants) {
        this(motor);
        this.setConstants(constants);
    }

    public SparkMaxPID(SparkMaxPIDController pidController) {
        this.pidController = pidController;
    }

    public SparkMaxPID(SparkMaxPIDController pidController, SparkMaxConstants constants) {
        this(pidController);
        this.setConstants(constants);
    }

    public void setConstants(SparkMaxConstants c) {
        this.slot = c.slot;
        
        pidController.setP(c.kP, c.slot);  
        pidController.setI(c.kI, c.slot);
        pidController.setD(c.kD, c.slot);
        pidController.setIZone(c.kIz, c.slot);
        pidController.setFF(c.kFF, c.slot);
        pidController.setOutputRange(c.kMinOutput, c.kMaxOutput, c.slot);
        pidController.setSmartMotionMinOutputVelocity(c.minVel, c.slot);
        pidController.setSmartMotionMaxVelocity(c.maxVel, c.slot);
        pidController.setSmartMotionMaxAccel(c.maxAcc, c.slot);
        pidController.setSmartMotionAllowedClosedLoopError(c.allowedErr, c.slot);
    }

    public SparkMaxConstants getConstants(int slot) {
        return new SparkMaxConstants(
            pidController.getP(slot), 
            pidController.getI(slot),
            pidController.getD(slot),
            pidController.getIZone(slot),
            pidController.getFF(slot), 
            pidController.getOutputMin(slot), 
            pidController.getOutputMax(slot), 
            this.slot, 
            pidController.getSmartMotionMinOutputVelocity(slot), 
            pidController.getSmartMotionMaxVelocity(slot),
            pidController.getSmartMotionMaxAccel(slot), 
            pidController.getSmartMotionAllowedClosedLoopError(slot));
    }

    public SparkMaxConstants getConstants() {
        return this.getConstants(this.slot);
    }

    public void setPosition(double setPoint) {
        pidController.setReference(setPoint, CANSparkMax.ControlType.kSmartMotion, this.slot);
    }

    public void setVelocity(double speed){
        pidController.setReference(speed, CANSparkMax.ControlType.kSmartVelocity, this.slot);
    }

}
