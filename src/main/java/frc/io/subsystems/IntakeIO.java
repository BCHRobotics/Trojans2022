package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

// Import required Classes
import frc.robot.Constants;

public class IntakeIO implements IIO{

    private static IntakeIO instance;

    private CANSparkMax intakeMotor;
    private CANSparkMax stagerMotor;
    private CANSparkMax feederMotor;

    private RelativeEncoder intakeEncoder;
    private RelativeEncoder stagerEncoder;

    private Compressor compressor;
    private Solenoid leftIntakeArm;
    private Solenoid rightIntakeArm;

    private boolean enabled = Constants.INTAKE_ENABLED; 

    public static IntakeIO getInstance() {
        if (instance == null) {
            instance = new IntakeIO();
        }
        return instance;
    }

    /**
     * Initiates the Intake / Stager / Feeder Output 
     */
    private IntakeIO() {
        if (!enabled) return;

        // Initiate Pneumatic systems
        this.compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
        this.leftIntakeArm = new Solenoid(0, PneumaticsModuleType.CTREPCM, Constants.LEFT_INTAKE_ARM);
        this.rightIntakeArm = new Solenoid(0, PneumaticsModuleType.CTREPCM, Constants.RIGHT_INTAKE_ARM);

        // Enable compressor
        this.compressor.enabled();

        // Initiate new motor objects
        this.intakeMotor = new CANSparkMax(Constants.INTKAE_ROLLER_ID, MotorType.kBrushless);
        this.stagerMotor = new CANSparkMax(Constants.STAGER_ROLLER_ID, MotorType.kBrushless);
        this.feederMotor = new CANSparkMax(Constants.FEEDER_ROLLER_ID, MotorType.kBrushed);

        // Get motor encoder
        this.intakeEncoder = intakeMotor.getEncoder();
        this.stagerEncoder = stagerMotor.getEncoder();

        // Restore motor controllers to factory defaults
        this.intakeMotor.restoreFactoryDefaults();
        this.stagerMotor.restoreFactoryDefaults();
        this.feederMotor.restoreFactoryDefaults();
        
        // Set motor controllers Idle Mode [Brake/Coast]
        this.intakeMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.stagerMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        this.feederMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        // Inversion state of motors
        this.intakeMotor.setInverted(false);
        this.stagerMotor.setInverted(false);
        this.feederMotor.setInverted(false);

        // Send out settings to controller
        this.intakeMotor.burnFlash();
        this.stagerMotor.burnFlash();
        this.feederMotor.burnFlash();
    }

    /**
     * Set the speed of the Intake
     * @param speed in decimal
     */
    public void setIntakeSpeed(double speed) {
        if (!enabled) return;
        this.intakeMotor.set(speed);
    }

    /**
     * Set the speed of the Intake
     * @param speed in decimal
     */
    public void setStagerSpeed(double speed) {
        if (!enabled) return;
        this.stagerMotor.set(speed);
    }

    /**
     * Set the speed of the Intake
     * @param speed in decimal
     */
    public void setFeederSpeed(double speed) {
        if (!enabled) return;
        this.feederMotor.set(speed);
    }

    /**
     * Set the position of the intake
     * @param state boolean state of intake. 
     * { FALSE: Raised | TRUE: Lowered }
     */
    public void setIntakeState(boolean state) {
        if (!enabled) return;
        this.leftIntakeArm.set(state);
        this.rightIntakeArm.set(state);
    }

    /**
     * Get the reference to the Intake encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getIntakeEncoder() {
        if (!enabled) return null;
        return this.intakeMotor.getEncoder();
    }

    /**
     * Get the reference to the Stager encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getStagerEncoder() {
        if (!enabled) return null;
        return this.stagerMotor.getEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        if (!enabled) return;
        this.intakeEncoder.setPosition(0);
        this.stagerEncoder.setPosition(0);
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
        this.intakeMotor.disable();
        this.stagerMotor.disable();
        this.feederMotor.disable();
    }
}
