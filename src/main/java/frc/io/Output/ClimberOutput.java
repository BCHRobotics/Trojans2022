package frc.io.Output;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ClimberOutput implements IRobotOutput {
    private CANSparkMax armExtendRight;
    private CANSparkMax armExtendLeft;

    private CANSparkMax armRotateRight;
    private CANSparkMax armRotateLeft;

    protected ClimberOutput() {
        this.armExtendRight = new CANSparkMax(20, MotorType.kBrushless);
        this.armRotateRight = new CANSparkMax(21, MotorType.kBrushless);

        this.armExtendLeft = new CANSparkMax(25, MotorType.kBrushless);
        this.armRotateLeft = new CANSparkMax(26, MotorType.kBrushless);

        // TODO: Before running code on robot, check motor direction
        this.armExtendRight.setInverted(false);
        this.armRotateRight.setInverted(false);

        this.armExtendLeft.setInverted(false);
        this.armRotateLeft.setInverted(false);
    }

    public void setArmExtend(double speed) {
        this.setArmExtendRight(speed);
        this.setArmExtendLeft(speed);
    }

    public void setArmRotate(double speed) {
        this.setArmRotateRight(speed);
        this.setArmRotateLeft(speed);
    }

    public void setArmExtendRight(double speed) {
        this.armExtendRight.set(speed);
    }

    public void setArmExtendLeft(double speed) {
        this.armExtendLeft.set(speed);
    }

    public void setArmRotateRight(double speed) {
        this.armExtendRight.set(speed);
    }

    public void setArmRotateLeft(double speed) {
        this.armRotateLeft.set(speed);
    }

    public CANEncoder getArmExtendRightEncoder() {
        return this.armExtendRight.getEncoder();
    }

    public CANEncoder getArmExtendLeftEncoder() {
        return this.armExtendLeft.getEncoder();
    }

    public CANEncoder getArmRotateRightEncoder() {
        return this.armRotateRight.getEncoder();
    }

    public CANEncoder getArmRotateLeftEncoder() {
        return this.armRotateLeft.getEncoder();
    }

    @Override
    public void stopAll() {
        setArmExtend(0);
        setArmRotate(0);
    }
    
}
