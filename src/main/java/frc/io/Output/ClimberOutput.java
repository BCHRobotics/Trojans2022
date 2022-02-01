package frc.io.Output;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ClimberOutput implements IRobotOutput {
    private static ClimberOutput instance;

    private CANSparkMax armExtendRight;
    private CANSparkMax armExtendLeft;

    private CANSparkMax armRotateRight;
    private CANSparkMax armRotateLeft;

    public static ClimberOutput getInstance(){
        if (instance == null) {
            instance = new ClimberOutput();
        }
        return instance;
    }

    /**
     * Initiates the Climber Output 
     */
    private ClimberOutput() {
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

    /**
     * Set the speed of both the Right and Left Extend Motors
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmExtend(double speed) {
        this.setArmExtendRight(speed);
        this.setArmExtendLeft(speed);
    }

    /**
     * Set the speed of both the Right and Left Rotate Motors
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmRotate(double speed) {
        this.setArmRotateRight(speed);
        this.setArmRotateLeft(speed);
    }

    /**
     * Set the speed of the Right Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmExtendRight(double speed) {
        this.armExtendRight.set(speed);
    }

    /**
     * Set the speed of the Left Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmExtendLeft(double speed) {
        this.armExtendLeft.set(speed);
    }

    /**
     * Set the speed of the Right Rotate Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmRotateRight(double speed) {
        this.armRotateRight.set(speed);
    }

    /**
     * Set the speed of the Left Rotate Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setArmRotateLeft(double speed) {
        this.armRotateLeft.set(speed);
    }

    /**
     * Get the reference to the Right Arm Extend Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getArmExtendRightEncoder() {
        return this.armExtendRight.getEncoder();
    }

    /**
     * Get the reference to the Left Arm Extend Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getArmExtendLeftEncoder() {
        return this.armExtendLeft.getEncoder();
    }

    /**
     * Get the reference to the Right Arm Rotate Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getArmRotateRightEncoder() {
        return this.armRotateRight.getEncoder();
    }

    /**
     * Get the reference to the Left Arm Rotate Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getArmRotateLeftEncoder() {
        return this.armRotateLeft.getEncoder();
    }

    /**
     * Stop all motors
     */
    @Override
    public void stopAll() {
        setArmExtend(0);
        setArmRotate(0);
    }
    
}
