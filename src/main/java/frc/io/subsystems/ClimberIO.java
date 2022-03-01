package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Import required Classes
import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ClimberIO implements IIO{

    private static ClimberIO instance;

    private CANSparkMax leftWinchMotor;
    private CANSparkMax rightWinchMotor;

    private RelativeEncoder leftWinchEncoder;
    private RelativeEncoder rightWinchEncoder;

    private SparkMaxPID leftWinchPidController;

    private SparkMaxConstants leftWinchConstants = Constants.WINCH_CONSTANTS;

    private boolean enabled = Constants.WINCH_ENABLED;

    public static ClimberIO getInstance() {
        if (instance == null) {
            instance = new ClimberIO();
        }
        return instance;
    }

    /**
     * Initiates the Winch Output 
     */
    private ClimberIO() {
        if (!enabled) return;

        // Initiate new Winch motor objects
        this.leftWinchMotor = new CANSparkMax(Constants.LEFT_WINCH_ID, MotorType.kBrushless);
        this.rightWinchMotor = new CANSparkMax(Constants.RIGHT_WINCH_ID, MotorType.kBrushless);

        // Get motor encoder
        this.leftWinchEncoder = leftWinchMotor.getEncoder();
        this.rightWinchEncoder = rightWinchMotor.getEncoder();

        // Restore motor controllers to factory defaults
        this.leftWinchMotor.restoreFactoryDefaults();
        this.rightWinchMotor.restoreFactoryDefaults();
        
        // Set motor controllers Idle Mode [Brake/Coast]
        this.leftWinchMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.rightWinchMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        // Set left Winch PID
        this.leftWinchPidController = new SparkMaxPID(leftWinchMotor);
        this.leftWinchPidController.activateSmartMotion();
        this.leftWinchPidController.setConstants(leftWinchConstants);

        // Inversion state of left Winch
        this.leftWinchMotor.setInverted(false);

        // Set right Winch to copy left Winch inversly
        this.rightWinchMotor.follow(leftWinchMotor, true);

        // Send out settings to controller
        this.leftWinchMotor.burnFlash();
        this.rightWinchMotor.burnFlash();
    }

    /**
     * Set the position of the Winch
     * @param setPoint in revolutions
     */
    public void setWinchExtension(double setPoint) {
        if (!enabled) return;
        if (setPoint < 0) {
            this.leftWinchMotor.disable();
            return;
        }
        this.leftWinchPidController.setPosition(setPoint);
    }

    /**
     * Get the reference to the Left Winch encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getLeftWinchEncoder() {
        if (!enabled) return null;
        return this.leftWinchMotor.getEncoder();
    }

    /**
     * Get the reference to the Right Winch encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getRightWinchEncoder() {
        if (!enabled) return null;
        return this.rightWinchMotor.getEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        if (!enabled) return;
        this.leftWinchEncoder.setPosition(0);
        this.rightWinchEncoder.setPosition(0);
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
        this.leftWinchMotor.disable();
        this.rightWinchMotor.disable();
    }
}
