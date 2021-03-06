package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Import required Classes
import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ArmIO implements IIO{

    private static ArmIO instance;

    private CANSparkMax leftArmMotor;
    private CANSparkMax rightArmMotor;

    private RelativeEncoder leftArmEncoder;
    private RelativeEncoder rightArmEncoder;

    private SparkMaxPID leftArmPidController;

    private SparkMaxConstants leftArmConstants = Constants.ARM_CONSTANTS;

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
        if (!enabled) return;

        // Initiate new arm motor objects
        this.leftArmMotor = new CANSparkMax(Constants.LEFT_ARM_ID, MotorType.kBrushless);
        this.rightArmMotor = new CANSparkMax(Constants.RIGHT_ARM_ID, MotorType.kBrushless);

        // Get motor encoder
        this.leftArmEncoder = leftArmMotor.getEncoder();
        this.rightArmEncoder = rightArmMotor.getEncoder();

        // Restore motor controllers to factory defaults
        this.leftArmMotor.restoreFactoryDefaults();
        this.rightArmMotor.restoreFactoryDefaults();
        
        // Set motor controllers Idle Mode [Brake/Coast]
        this.leftArmMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.rightArmMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        // Set Arm PID
        this.leftArmPidController = new SparkMaxPID(leftArmMotor);
        this.leftArmPidController.setConstants(leftArmConstants);

        // Inversion state of left arm
        this.leftArmMotor.setInverted(false);

        // Set right arm to copy left arm inversly
        this.rightArmMotor.follow(leftArmMotor, true);

        // Send out settings to controller
        this.leftArmMotor.burnFlash();
        this.rightArmMotor.burnFlash();
    }

    /**
     * Set the position of the Lift Arm
     * @param setPoint in revolutions
     */
    public void setArmPosition(double setPoint) {
        if (!enabled) return;
        this.leftArmPidController.setPosition(setPoint);
    }

    /**
     * Get the reference to the Left Arm encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getLeftArmEncoder() {
        if (!enabled) return null;
        return this.leftArmMotor.getEncoder();
    }

    /**
     * Get the reference to the Right Arm encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getRightArmEncoder() {
        if (!enabled) return null;
        return this.rightArmMotor.getEncoder();
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
