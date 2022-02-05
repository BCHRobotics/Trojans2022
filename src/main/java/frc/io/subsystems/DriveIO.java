package frc.io.subsystems;

// Imports for motor outputs
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

    public static DriveIO getInstance() {
        if(instance == null) instance = new DriveIO();
        return instance;
    }

    private DriveIO() {
        this.driveL1 = new CANSparkMax(11, MotorType.kBrushless);   
        this.driveL2 = new CANSparkMax(12, MotorType.kBrushless);   
        this.driveR1 = new CANSparkMax(15, MotorType.kBrushless);
        this.driveR2 = new CANSparkMax(16, MotorType.kBrushless);
        
        this.driveL1Encoder = driveL1.getEncoder();
        this.driveL2Encoder = driveL1.getEncoder();
        this.driveR1Encoder = driveL1.getEncoder();
        this.driveR2Encoder = driveL1.getEncoder();

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
        setDriveLeft(0);
        setDriveRight(0);
    }
}