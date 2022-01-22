package frc.io.Output;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ClimberOutput implements IRobotOutput {
    private CANSparkMax armExtend;
    private CANSparkMax armRotate;

    protected ClimberOutput() {
        this.armExtend = new CANSparkMax(21, MotorType.kBrushless);
        this.armRotate = new CANSparkMax(22, MotorType.kBrushless);

        this.armExtend.setInverted(false);
        this.armRotate.setInverted(false);
    }

    public void setArmExtend(double speed) {
        this.armExtend.set(speed);
    }

    public void setArmRotate(double speed) {
        this.armRotate.set(speed);
    }

    public CANEncoder getArmExtendEncoder() {
        return this.armExtend.getEncoder();
    }

    public CANEncoder getArmRotateEncoder() {
        return this.armRotate.getEncoder();
    }

    @Override
    public void stopAll() {
        setArmExtend(0);
        setArmRotate(0);
    }
    
}
