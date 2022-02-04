package frc.io.Output;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxPIDController;

import frc.robot.Constants;

public class ShooterOutput implements IRobotOutput {

    private static final int wheelID = 22;
    private static final int turretID = 21;

    private static ShooterOutput instance;

    private boolean enabled = Constants.SHOOTER_ENABLED;

    private CANSparkMax wheelMotor;
    private CANSparkMax turretMotor;

    private SparkMaxPIDController turretPidController;

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

    public static ShooterOutput getInstance() {
        if (instance == null) {
            instance = new ShooterOutput();
        }
        return instance;
    }

    /**
     * Initiates the Climber Output 
     */
    private ShooterOutput() {
        if (!enabled) return;

        this.wheelMotor = new CANSparkMax(wheelID, MotorType.kBrushless);
        this.turretMotor = new CANSparkMax(turretID, MotorType.kBrushless);

        this.wheelMotor.restoreFactoryDefaults();
        this.turretMotor.restoreFactoryDefaults();

        this.turretPidController = turretMotor.getPIDController();

        // PID coefficients
        kP = 5e-5; 
        kI = 1e-6;
        kD = 0; 
        kIz = 0; 
        kFF = 0.000156; 
        kMaxOutput = 1; 
        kMinOutput = -1;
        maxRPM = 5700;

        // Smart Motion Coefficients
        maxVel = 6000; // rpm
        maxAcc = 3000;

        // set PID Coefficients
        turretPidController.setP(kP);
        turretPidController.setI(kI);
        turretPidController.setD(kD);
        turretPidController.setIZone(kIz);
        turretPidController.setFF(kFF);
        turretPidController.setOutputRange(kMinOutput, kMaxOutput);

        //set Smart Motion Coefficients
        int smartMotionSlot = 0;
        turretPidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
        turretPidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
        turretPidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
        turretPidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

        //Before running code on robot, check motor direction
        this.wheelMotor.setInverted(false);
        this.turretMotor.setInverted(false);

    }

    /**
     * Set the speed of the Right Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setWheelSpeed(double speed) {
        if (!enabled) return;
        this.wheelMotor.set(speed);
    }

    /**
     * Set the speed of the Left Extend Motor
     * @param speed speed in percent (-1 to 1)
     */
    public void setTurretPosition(double setPoint) {
        if (!enabled) return;
        this.turretPidController.setReference(setPoint, CANSparkMax.ControlType.kSmartMotion);
    }

    /**
     * Get the reference to the Right Arm Extend Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getWheelEncoder() {
        if (!enabled) return null;
        return this.wheelMotor.getEncoder();
    }

    /**
     * Get the reference to the Left Arm Extend Encoder
     * @return CANEncoder reference
     */
    public RelativeEncoder getTurretEncoder() {
        if (!enabled) return null;
        return this.turretMotor.getEncoder();
    }

    /**
     * Stop all motors
     */
    @Override
    public void stopAll() {
        setWheelSpeed(0);
        setTurretPosition(0);
    }
    
}
