package frc.io.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ShooterIO implements IIO{

    private static ShooterIO instance;

    private CANSparkMax wheelMotor;
    private CANSparkMax turretMotor;

    private RelativeEncoder wheelEncoder;
    private RelativeEncoder turretEncoder;

    private SparkMaxPID wheelPidController;
    private SparkMaxPID turretPidController;

    private SparkMaxConstants wheelConstants = Constants.WHEEL_CONSTANTS;
    private SparkMaxConstants turretConstants = Constants.TURRET_CONSTANTS;

    private boolean enabled = Constants.SHOOTER_ENABLED;

    public static ShooterIO getInstance() {
        if (instance == null) {
            instance = new ShooterIO();
        }
        return instance;
    }

    /**
     * Initiates the Climber Output 
     */
    private ShooterIO() {
        if (!enabled) return;

        this.wheelMotor = new CANSparkMax(Constants.wheelID, MotorType.kBrushless);
        this.turretMotor = new CANSparkMax(Constants.turretID, MotorType.kBrushless);

        this.wheelEncoder = wheelMotor.getEncoder();
        this.turretEncoder = turretMotor.getEncoder();

        this.wheelMotor.restoreFactoryDefaults();
        this.turretMotor.restoreFactoryDefaults();
        
        this.wheelMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.turretMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

        this.wheelPidController = new SparkMaxPID(wheelMotor);
        this.turretPidController = new SparkMaxPID(turretMotor);
        
        this.wheelPidController.setConstants(wheelConstants);
        this.turretPidController.setConstants(turretConstants);

        // Before running code on robot, check motor direction
        this.wheelMotor.setInverted(false);
        this.turretMotor.setInverted(true);

        this.wheelMotor.burnFlash();
        this.turretMotor.burnFlash();
    }

    /**
     * Set the speed of the Shooter Wheel Motor
     * @param speed speed in rpm
     */
    public void setWheelSpeed(double speed) {
        if (!enabled) return;
        this.wheelPidController.setVelocity(speed);
    }

    /**
     * Set the position of the Shooter Turret Motor
     * @param setPoint position in revolutions
     */
    public void setTurretPosition(double setPoint) {
        if (!enabled) return;
        this.turretPidController.setPosition(setPoint);
    }

    /**
     * Get the reference to the Shooter Wheel Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getWheelEncoder() {
        if (!enabled) return null;
        return this.wheelMotor.getEncoder();
    }

    /**
     * Get the reference to the Shooter Turret Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getTurretEncoder() {
        if (!enabled) return null;
        return this.turretMotor.getEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        if (!enabled) return;
        this.wheelEncoder.setPosition(0);
        this.turretEncoder.setPosition(0);
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
        this.wheelMotor.disable();
        this.turretMotor.disable();
    }
}
