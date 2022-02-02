package frc.io.Output;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

public class DriveOutput implements IRobotOutput {
    private static DriveOutput instance;

    private boolean enabled = Constants.DRIVE_ENABLED;

    private CANSparkMax driveL1;
    private CANSparkMax driveL2;
    private CANSparkMax driveR1;
    private CANSparkMax driveR2;

    public static DriveOutput getInstance() {
        if (instance == null) {
            instance = new DriveOutput();
        }
        return instance;
    }

    private DriveOutput() {
        if (!enabled) return;
            
        this.driveL1 = new CANSparkMax(11, MotorType.kBrushless);   
        this.driveL2 = new CANSparkMax(12, MotorType.kBrushless);   
        this.driveR1 = new CANSparkMax(15, MotorType.kBrushless);
        this.driveR2 = new CANSparkMax(16, MotorType.kBrushless); 

        this.driveL1.setInverted(true);
        this.driveR1.setInverted(false);

        this.driveL2.follow(this.driveL1, false);
        this.driveR2.follow(this.driveR1, false);

        this.driveL1.setSmartCurrentLimit(60, 10);
        this.driveL2.setSmartCurrentLimit(60, 10);
        this.driveR1.setSmartCurrentLimit(60, 10);
        this.driveR2.setSmartCurrentLimit(60, 10);
    }

    public void setDriveLeft(double speed) {
        if (!enabled) return;
        this.driveL1.set(speed);
    }

    public void setDriveRight(double speed) {
        if (!enabled) return;
        this.driveR1.set(speed);
    }

    public void setDriveRampRate(double rampRateSecondsToFull) {
        if (!enabled) return;
        this.driveL1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveL2.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR2.setOpenLoopRampRate(rampRateSecondsToFull);
    }

    public RelativeEncoder getDriveL1Encoder() {
        if (!enabled) return null;
        return this.driveL1.getEncoder();
    }

    public RelativeEncoder getDriveL2Encoder() {
        if (!enabled) return null;
        return this.driveL2.getEncoder();
    }

    public RelativeEncoder getDriveR1Encoder() {
        if (!enabled) return null;
        return this.driveR1.getEncoder();
    }

    public RelativeEncoder getDriveR2Encoder() {
        if (!enabled) return null;
        return this.driveR2.getEncoder();
    }

    @Override
    public void stopAll() {
        setDriveLeft(0);
        setDriveRight(0);
    }
}