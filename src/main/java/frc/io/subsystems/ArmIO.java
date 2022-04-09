package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

// Import required Classes
import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ArmIO implements IIO {

    private static ArmIO instance;

    private CANSparkMax leftArmMotor;
    private CANSparkMax rightArmMotor;

    private RelativeEncoder leftArmEncoder;
    private RelativeEncoder rightArmEncoder;

    private DutyCycleEncoder leftExternalEncoder;
    private DutyCycleEncoder rightExternalEncoder;

    private SparkMaxPID leftArmPidController;
    private SparkMaxPID rightArmPidController;

    private SparkMaxConstants leftArmConstants = Constants.LEFT_ARM_CONSTANTS;
    private SparkMaxConstants rightArmConstants = Constants.RIGHT_ARM_CONSTANTS;

    private DigitalInput leftArmLimitSwitch;
    private DigitalInput rightArmLimitSwitch;

    private boolean enabled = Constants.ARM_ENABLED;

    public static ArmIO getInstance() {
        if (instance == null) {
            instance = new ArmIO();
        }
        return instance;
    }

    /**
     * Initiates the Lift Output
     */
    private ArmIO() {
        if (!enabled)
            return;

        // Initiate new arm motor objects
        this.leftArmMotor = new CANSparkMax(Constants.LEFT_ARM_ID, MotorType.kBrushless);
        this.rightArmMotor = new CANSparkMax(Constants.RIGHT_ARM_ID, MotorType.kBrushless);

        this.leftArmLimitSwitch = new DigitalInput(Constants.LEFT_LIMIT_SWITCH_PORT);
        this.rightArmLimitSwitch = new DigitalInput(Constants.RIGHT_LIMIT_SWITCH_PORT);

        // Get motor encoder
        this.leftArmEncoder = leftArmMotor.getEncoder();
        this.rightArmEncoder = rightArmMotor.getEncoder();

        // Get external encoders
        this.leftExternalEncoder = new DutyCycleEncoder(Constants.LEFT_EXTERNAL_ENCODER_PORT);
        this.rightExternalEncoder = new DutyCycleEncoder(Constants.RIGHT_EXTERNAL_ENCODER_PORT);

        // Restore motor controllers to factory defaults
        this.leftArmMotor.restoreFactoryDefaults();
        this.rightArmMotor.restoreFactoryDefaults();

        // Set motor controllers Idle Mode [Brake/Coast]
        this.leftArmMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.rightArmMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        // Set Arm PID
        this.leftArmPidController = new SparkMaxPID(leftArmMotor);
        this.rightArmPidController = new SparkMaxPID(rightArmMotor);
        this.leftArmPidController.setConstants(leftArmConstants);
        this.rightArmPidController.setConstants(rightArmConstants);

        // Inversion state of left arm
        this.leftArmMotor.setInverted(false);
        this.rightArmMotor.setInverted(true);

        // Send out settings to controller
        this.leftArmMotor.burnFlash();
        this.rightArmMotor.burnFlash();
    }

    /**
     * Set the position of the Lift Arm
     * 
     * @param setPoint in revolutions
     */
    public void setArmPosition(double setPoint) {
        if (setPoint > Constants.ANGLE_LIMIT || setPoint < 0 || !enabled) return;

        SmartDashboard.putBoolean("Left limit", this.leftArmLimitSwitch.get());
        SmartDashboard.putBoolean("Right limit", this.rightArmLimitSwitch.get());
        if (this.leftArmLimitSwitch.get()) {
            if (this.leftArmEncoder.getPosition() != Constants.ANGLE_LIMIT) this.leftArmEncoder.setPosition(Constants.ANGLE_LIMIT);
            else this.leftArmPidController.setPosition(setPoint);
        } else this.leftArmPidController.setPosition(setPoint);

        if (this.rightArmLimitSwitch.get()) {
            if (this.rightArmEncoder.getPosition() != Constants.ANGLE_LIMIT) this.rightArmEncoder.setPosition(Constants.ANGLE_LIMIT);
            else this.rightArmPidController.setPosition(setPoint);
        } else this.rightArmPidController.setPosition(setPoint);

        if (this.leftArmEncoder.getPosition() > Constants.ANGLE_LIMIT || this.rightArmEncoder.getPosition() > Constants.ANGLE_LIMIT) {
            this.stopAllOutputs();
        }
    }

    /**
     * Get the reference to the Left Arm encoder
     * 
     * @return CANEncoder reference
     */
    public RelativeEncoder getLeftArmEncoder() {
        if (!enabled) return null;
        return this.leftArmMotor.getEncoder();
    }

    /**
     * Get the reference to the Right Arm encoder
     * 
     * @return CANEncoder reference
     */
    public RelativeEncoder getRightArmEncoder() {
        if (!enabled) return null;
        return this.rightArmMotor.getEncoder();
    }

    /**
     * Get the reference to the Left Arm External encoder
     * 
     * @return DutyCycleEncoder reference
     */
    public DutyCycleEncoder getLeftExternalEncoder() {
        if (!enabled) return null;
        return this.leftExternalEncoder;
    }

    /**
     * Get the reference to the Right Arm External encoder
     * 
     * @return DutyCycleEncoder reference
     */
    public DutyCycleEncoder getRightExternalEncoder() {
        if (!enabled) return null;
        return this.rightExternalEncoder;
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        if (!enabled) return;
        this.leftArmEncoder.setPosition(0);
        this.rightArmEncoder.setPosition(0);
    }

    /**
     * Reset the position of external encoders
     */
    public void resetExternalEncoders() {
        if (!enabled) return; 
        this.leftExternalEncoder.reset();
        this.rightExternalEncoder.reset();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void updateInputs() {
        if (!enabled) return;
    }

    /**
     * Stop all motors
     */
    @Override
    public void stopAllOutputs() {
        if (!enabled) return;
        this.leftArmMotor.disable();
        this.rightArmMotor.disable();
    }
}
