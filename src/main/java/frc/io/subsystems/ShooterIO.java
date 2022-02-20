package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Import required Classes
import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ShooterIO implements IIO {

    private static ShooterIO instance;

    private CANSparkMax leftWheelMotor;
    private CANSparkMax rightWheelMotor;

    private RelativeEncoder leftWheelEncoder;
    private RelativeEncoder rightWheelEncoder;

    private SparkMaxPID leftWheelPidController;

    private SparkMaxConstants leftWheelConstants = Constants.SHOOTER_WHEEL_CONSTANTS;

    private boolean enabled = Constants.SHOOTER_ENABLED;

    public static ShooterIO getInstance() {
        if (instance == null) {
            instance = new ShooterIO();
        }
        return instance;
    }

    /**
     * Initiates the Shooter Output 
     */
    private ShooterIO() {
        if (!enabled) return;

        // Initiate new arm motor objects
        this.leftWheelMotor = new CANSparkMax(Constants.LEFT_SHOOTER_WHEEL_ID, MotorType.kBrushless);
        this.rightWheelMotor = new CANSparkMax(Constants.RIGHT_SHOOTER_WHEEL_ID, MotorType.kBrushless);

        // Get motor encoder
        this.leftWheelEncoder = leftWheelMotor.getEncoder();
        this.rightWheelEncoder = rightWheelMotor.getEncoder();

        // Restore motor controllers to factory defaults
        this.leftWheelMotor.restoreFactoryDefaults();
        this.rightWheelMotor.restoreFactoryDefaults();
        
        // Set motor controllers Idle Mode [Brake/Coast]
        this.leftWheelMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.rightWheelMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

        // Set Wheel PID
        this.leftWheelPidController = new SparkMaxPID(leftWheelMotor);
        this.leftWheelPidController.setConstants(leftWheelConstants);

        // Inversion state of shooter wheels
        this.leftWheelMotor.setInverted(false);

        // right wheel to copy left wheel inversly
        this.rightWheelMotor.follow(leftWheelMotor, true);

        // Send out settings to 
        this.leftWheelMotor.burnFlash();
        this.rightWheelMotor.burnFlash();
    }

    /**
     * Set the speed of the Shooter Wheel Motor
     * @param speed in rpm
     */
    public void setWheelSpeed(double speed) {
        if (!enabled) return;
        this.leftWheelPidController.setVelocity(speed);
    }

    /**
     * Get the reference to the Shooter Wheel Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getLeftWheelEncoder() {
        if (!enabled) return null;
        return this.leftWheelMotor.getEncoder();
    }

    /**
     * Get the reference to the Shooter Turret Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getRightWheelEncoder() {
        if (!enabled) return null;
        return this.rightWheelMotor.getEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        if (!enabled) return;
        this.leftWheelEncoder.setPosition(0);
        this.rightWheelEncoder.setPosition(0);
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
        this.leftWheelMotor.disable();
        this.rightWheelMotor.disable();
    }
}
