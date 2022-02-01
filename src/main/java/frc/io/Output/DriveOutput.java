package frc.io.Output;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveOutput implements IRobotOutput {
    private static DriveOutput instance;

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
        /*this.driveL1 = new CANSparkMax(10, MotorType.kBrushless);   
        this.driveL2 = new CANSparkMax(11, MotorType.kBrushless);   
        this.driveR1 = new CANSparkMax(12, MotorType.kBrushless);
        this.driveR2 = new CANSparkMax(13, MotorType.kBrushless); 

        this.driveL1.setInverted(true);
        this.driveR1.setInverted(false);

        this.driveL2.follow(this.driveL1, false);
        this.driveR2.follow(this.driveR1, false);

        this.driveL1.setSmartCurrentLimit(60, 10);
        this.driveL2.setSmartCurrentLimit(60, 10);
        this.driveR1.setSmartCurrentLimit(60, 10);
        this.driveR2.setSmartCurrentLimit(60, 10);*/
    }

    public void setDriveLeft(double speed) {
        //this.driveL1.set(speed);
    }

    public void setDriveRight(double speed) {
        //this.driveR1.set(speed);
    }

    public void setDriveRampRate(double rampRateSecondsToFull) {
        /*this.driveL1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveL2.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR1.setOpenLoopRampRate(rampRateSecondsToFull);
        this.driveR2.setOpenLoopRampRate(rampRateSecondsToFull);*/
    }

    public RelativeEncoder getDriveL1Encoder() {
        //return this.driveL1.getEncoder();
        return null;
    }

    public RelativeEncoder getDriveL2Encoder() {
        //return this.driveL2.getEncoder();
        return null;
    }

    public RelativeEncoder getDriveR1Encoder() {
        //return this.driveR1.getEncoder();
        return null;
    }

    public RelativeEncoder getDriveR2Encoder() {
        //return this.driveR2.getEncoder();
        return null;
    }

    @Override
    public void stopAll() {
        setDriveLeft(0);
        setDriveRight(0);
    }
}