package frc.io.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class ShooterIO implements IIO{

    private static ShooterIO instance;

    private CANSparkMax leftWheelMotor;
    private CANSparkMax rightWheelMotor;

    private RelativeEncoder leftWheelEncoder;
    private RelativeEncoder rightWheelEncoder;

    private SparkMaxPID leftWheelPidController;
    private SparkMaxPID rightWheelPidController;

    private SparkMaxConstants leftWheelConstants = Constants.LEFT_WHEEL_CONSTANTS;
    private SparkMaxConstants rightWheelConstants = Constants.RIGHT_WHEEL_CONSTANTS;

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

        this.leftWheelMotor = new CANSparkMax(Constants.wheelID, MotorType.kBrushless);
        this.rightWheelMotor = new CANSparkMax(Constants.turretID, MotorType.kBrushless);

        this.leftWheelEncoder = leftWheelMotor.getEncoder();
        this.rightWheelEncoder = rightWheelMotor.getEncoder();

        this.leftWheelMotor.restoreFactoryDefaults();
        this.rightWheelMotor.restoreFactoryDefaults();
        
        this.leftWheelMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.rightWheelMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);

        this.leftWheelPidController = new SparkMaxPID(leftWheelMotor);
        this.rightWheelPidController = new SparkMaxPID(rightWheelMotor);
        
        this.leftWheelPidController.setConstants(leftWheelConstants);
        this.rightWheelPidController.setConstants(rightWheelConstants);

        // Before running code on robot, check motor direction
        this.leftWheelMotor.setInverted(false);
        this.rightWheelMotor.setInverted(true);

        this.leftWheelMotor.burnFlash();
        this.rightWheelMotor.burnFlash();
    }

    /**
     * Set the speed of the Shooter Wheel Motor
     * @param speed speed in rpm
     */
    public void setWheelSpeed(double speed) {
        if (!enabled) return;
        this.leftWheelPidController.setVelocity(speed);
        this.rightWheelPidController.setVelocity(speed);
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
