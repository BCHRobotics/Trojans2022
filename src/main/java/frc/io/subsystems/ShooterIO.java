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

        this.wheelMotor = new CANSparkMax(Constants.wheelID, MotorType.kBrushless);
        this.turretMotor = new CANSparkMax(Constants.turretID, MotorType.kBrushless);

        this.wheelEncoder = wheelMotor.getEncoder();
        this.turretEncoder = turretMotor.getEncoder();

        this.wheelMotor.restoreFactoryDefaults();
        this.turretMotor.restoreFactoryDefaults();
        
        this.wheelMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        this.wheelPidController = new SparkMaxPID(wheelMotor);
        this.turretPidController = new SparkMaxPID(turretMotor);
        
        // set wheel PID Coefficients
        this.wheelPidController.setPID(wheelConstants.kP, wheelConstants.kI, wheelConstants.kD, wheelConstants.kIz, wheelConstants.kFF, wheelConstants.kMinOutput, wheelConstants.kMaxOutput);

        //set wheel Smart Motion Coefficients
        this.wheelPidController.setSmartMotion(wheelConstants.slot, wheelConstants.minVel, wheelConstants.maxVel, wheelConstants.maxAcc, wheelConstants.allowedErr);

        // set turret PID Coefficients
        this.turretPidController.setPID(turretConstants.kP, turretConstants.kI, turretConstants.kD, turretConstants.kIz, turretConstants.kFF, turretConstants.kMinOutput, turretConstants.kMaxOutput);

        //set turret Smart Motion Coefficients
        this.turretPidController.setSmartMotion(turretConstants.slot, turretConstants.minVel, turretConstants.maxVel, turretConstants.maxAcc, turretConstants.allowedErr);

        //Before running code on robot, check motor direction
        this.wheelMotor.setInverted(false);
        this.turretMotor.setInverted(false);

    }

    /**
     * Set the speed of the Shooter Wheel Motor
     * @param speed speed in rpm
     */
    public void setWheelSpeed(double speed) {
        this.wheelPidController.setVelocity(speed);
    }

    /**
     * Set the position of the Shooter Turret Motor
     * @param setPoint position in revolutions
     */
    public void setTurretPosition(double setPoint) {
        this.turretPidController.setPosition(setPoint);
    }

    /**
     * Get the reference to the Shooter Wheel Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getWheelEncoder() {
        return this.wheelMotor.getEncoder();
    }

    /**
     * Get the reference to the Shooter Turret Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getTurretEncoder() {
        return this.turretMotor.getEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        this.wheelEncoder.setPosition(0);
        this.turretEncoder.setPosition(0);
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void updateInputs() {
    }

    /**
     * Stop all motors
     */
    @Override
    public void stopAllOutputs() {
        this.wheelMotor.disable();
        this.turretMotor.disable();
    }
}
