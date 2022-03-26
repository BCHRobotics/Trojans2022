package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private DigitalInput armLimitSwitch;

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

        this.armLimitSwitch = new DigitalInput(9);

        // Get motor encoder
        this.leftArmEncoder = leftArmMotor.getEncoder();
        this.rightArmEncoder = rightArmMotor.getEncoder();

        // Restore motor controllers to factory defaults
        this.leftArmMotor.restoreFactoryDefaults();
        this.rightArmMotor.restoreFactoryDefaults();
        
        // Set motor controllers Idle Mode [Brake/Coast]
        this.leftArmMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.rightArmMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

        // Set Arm PID
        this.leftArmPidController = new SparkMaxPID(leftArmMotor);
        this.leftArmPidController.activateSmartMotion();
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
        if (setPoint < 0) return;
        SmartDashboard.putBoolean("Limit switch", this.armLimitSwitch.get());
        if (!this.armLimitSwitch.get()) {
            if (this.leftArmEncoder.getPosition() != Constants.ANGLE_LIMIT) {
                this.leftArmEncoder.setPosition(Constants.ANGLE_LIMIT);
                this.rightArmEncoder.setPosition(Constants.ANGLE_LIMIT);
            } else {
                this.leftArmPidController.setPosition(setPoint);
            }
        } else {
            this.leftArmPidController.setPosition(setPoint);
        }

        if (this.leftArmEncoder.getPosition() >= 45) {
            this.stopAllOutputs();
        }
    }

    public void setPercentOut(double input) {
        this.leftArmMotor.set(input);
        this.rightArmMotor.set(input);
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
