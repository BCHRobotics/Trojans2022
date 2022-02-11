package frc.util.pid;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

public class SparkMaxPID {

    private SparkMaxPIDController pidController;
    private CANSparkMax motor;

    public SparkMaxPID(CANSparkMax motor) {
        this.motor = motor;
        pidController = this.motor.getPIDController();
    }

    public void setPID(double kP, double kI, double kD, double kIz, double kFF, double kMinOutput, double kMaxOutput){
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
        pidController.setFF(kFF);
        pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    public void setSmartMotion(int smartMotionSlot, double minVel, double maxVel, double maxAcc, double allowedErr){  
        pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    }

    public void setPosition(double setPoint) {
        System.out.println(pidController.setReference(setPoint, CANSparkMax.ControlType.kSmartMotion));
    }

    public void setVelocity(double speed){
        pidController.setReference(speed, CANSparkMax.ControlType.kSmartVelocity);
    }

}
