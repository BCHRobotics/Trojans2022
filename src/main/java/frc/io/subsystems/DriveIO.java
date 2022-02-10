package frc.io.subsystems;

// Imports for motor outputs
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class DriveIO implements IIO {
    private static DriveIO instance;

    // Drive motors
    private CANSparkMax driveL1;
    private CANSparkMax driveL2;
    private CANSparkMax driveR1;
    private CANSparkMax driveR2;
    
    // Drive encoders
    private RelativeEncoder driveL1Encoder;
    private RelativeEncoder driveL2Encoder;
    private RelativeEncoder driveR1Encoder;
    private RelativeEncoder driveR2Encoder;

    // PID Controllers
    private SparkMaxPID driveL1PidController;
    private SparkMaxPID driveR1PidController;

    // PID Constants
    private SparkMaxConstants driveL1Constants = Constants.DRIVEL1_CONSTANTS;
    private SparkMaxConstants driveR1Constants = Constants.DRIVER1_CONSTANTS;

    public static DriveIO getInstance() {
        if(instance == null) instance = new DriveIO();
        return instance;
    }

    private DriveIO() {
        this.driveL1 = new CANSparkMax(Constants.driveL1ID, MotorType.kBrushless);   
        this.driveL2 = new CANSparkMax(Constants.driveL2ID, MotorType.kBrushless);   
        this.driveR1 = new CANSparkMax(Constants.driveR1ID, MotorType.kBrushless);
        this.driveR2 = new CANSparkMax(Constants.driveR2ID, MotorType.kBrushless);
        
        this.driveL1Encoder = driveL1.getEncoder();
        this.driveL2Encoder = driveL1.getEncoder();
        this.driveR1Encoder = driveL1.getEncoder();
        this.driveR2Encoder = driveL1.getEncoder();

        this.driveL1.restoreFactoryDefaults();
        this.driveL2.restoreFactoryDefaults();
        this.driveR1.restoreFactoryDefaults();
        this.driveR2.restoreFactoryDefaults();

        this.driveL1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.driveL2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.driveR1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.driveR2.setIdleMode(CANSparkMax.IdleMode.kCoast);

        // when factor 1 travels: 46.4
        double driveFactor = 100 / 46.4;
        this.driveL1Encoder.setPositionConversionFactor(driveFactor);
        this.driveL2Encoder.setPositionConversionFactor(driveFactor);
        this.driveR1Encoder.setPositionConversionFactor(driveFactor);
        this.driveR2Encoder.setPositionConversionFactor(driveFactor);

        // set motor current limits
        this.driveL1.setSmartCurrentLimit(60, 10);
        this.driveL2.setSmartCurrentLimit(60, 10);
        this.driveR1.setSmartCurrentLimit(60, 10);
        this.driveR2.setSmartCurrentLimit(60, 10);

        // set PID controllers
        this.driveL1PidController = new SparkMaxPID(driveL1);
        this.driveR1PidController = new SparkMaxPID(driveR1);

        // set Left Motor PID Coefficients
        this.driveL1PidController.setPID(driveL1Constants.kP, driveL1Constants.kI, driveL1Constants.kD, driveL1Constants.kIz, driveL1Constants.kFF, driveL1Constants.kMinOutput, driveL1Constants.kMaxOutput);

        //set Left Motor Smart Motion Coefficients
        this.driveL1PidController.setSmartMotion(driveL1Constants.slot, driveL1Constants.minVel, driveL1Constants.maxVel, driveL1Constants.maxAcc, driveL1Constants.allowedErr);

        // set Right Motor PID Coefficients
        this.driveR1PidController.setPID(driveR1Constants.kP, driveR1Constants.kI, driveR1Constants.kD, driveR1Constants.kIz, driveR1Constants.kFF, driveR1Constants.kMinOutput, driveR1Constants.kMaxOutput);

        //set Right Motor Smart Motion Coefficients
        this.driveR1PidController.setSmartMotion(driveR1Constants.slot, driveR1Constants.minVel, driveR1Constants.maxVel, driveR1Constants.maxAcc, driveR1Constants.allowedErr);


        this.driveL1.setInverted(true);
        this.driveR1.setInverted(false);

        this.driveL2.follow(this.driveL1, false);
        this.driveR2.follow(this.driveR1, false);
    }

    public void setDriveLeft(double speed) {
        this.driveL1.set(speed);
    }

    public void setDriveRight(double speed) {
        this.driveR1.set(speed);
    }

    public void setDriveLeftPos(double position) {
        this.driveL1PidController.setPosition(position);
    }

    public void setDriveRightPos(double position) {
        this.driveR1PidController.setPosition(position);
    }

    public void setDriveRampRate(double rampRateSecondsToFull) {
        this.driveL1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveL2.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR2.setOpenLoopRampRate(rampRateSecondsToFull);
    }

    //#region EncoderPositions

    public double getDriveL1Encoder() {
        return this.driveL1Encoder.getPosition();
    }

    public double getDriveL2Encoder() {
        return this.driveL2Encoder.getPosition();
    }

    public double getDriveR1Encoder() {
        return this.driveR1Encoder.getPosition();
    }

    public double getDriveR2Encoder() {
        return this.driveR2Encoder.getPosition();
    }

    //#endregion EncoderPositions

    @Override
    public void resetInputs() {
        this.driveL1Encoder.setPosition(0);
        this.driveL2Encoder.setPosition(0);
        this.driveR1Encoder.setPosition(0);
        this.driveR2Encoder.setPosition(0);
    }

    @Override
    public void updateInputs() {
       
    }

    @Override
    public void stopAllOutputs() {
        this.driveL1.disable();
        this.driveL2.disable();
        this.driveR1.disable();
        this.driveR2.disable();
    }
}