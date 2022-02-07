package frc.io.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.util.pid.SparkMaxPID;

public class ShooterIO implements IIO{
    private static final int wheelID = 22;
    private static final int turretID = 21;

    private static ShooterIO instance;

    private CANSparkMax wheelMotor;
    private CANSparkMax turretMotor;

    private RelativeEncoder wheelEncoder;
    private RelativeEncoder turretEncoder;

    private SparkMaxPID wheelPidController;
    private SparkMaxPID turretPidController;

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

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

        this.wheelMotor = new CANSparkMax(wheelID, MotorType.kBrushless);
        this.turretMotor = new CANSparkMax(turretID, MotorType.kBrushless);

        this.wheelEncoder = wheelMotor.getEncoder();
        this.turretEncoder = turretMotor.getEncoder();

        this.wheelMotor.restoreFactoryDefaults();
        this.turretMotor.restoreFactoryDefaults();

        this.wheelPidController = new SparkMaxPID(wheelMotor);
        this.turretPidController = new SparkMaxPID(turretMotor);

        // PID coefficients
        kP = 5e-5; 
        kI = 1e-6;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000156; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        // Smart Motion Coefficients
        maxVel = 2000; // rpm
        maxAcc = 1500;

        // set PID Coefficients
        this.turretPidController.setPID(kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput);

        //set Smart Motion Coefficients
        this.turretPidController.setSmartMotion(0, maxRPM, minVel, maxVel, maxAcc, allowedErr);

        //Before running code on robot, check motor direction
        this.wheelMotor.setInverted(false);
        this.turretMotor.setInverted(true);

    }

    /**
     * Set the speed of the Right Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setWheelSpeed(double speed) {
        this.wheelPidController.setVelocity(speed);
    }

    /**
     * Set the speed of the Left Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setTurretPosition(double setPoint) {
        this.turretPidController.setPosition(setPoint);
    }

    /**
     * Get the reference to the Right Arm Extend Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getWheelEncoder() {
        return this.wheelMotor.getEncoder();
    }

    /**
     * Get the reference to the Left Arm Extend Encoder
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
        setWheelSpeed(0);
        setTurretPosition(0);
    }
}
