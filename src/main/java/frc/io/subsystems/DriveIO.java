package frc.io.subsystems;

// Import required Libraries
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

// Import required Classes
import frc.robot.Constants;

public class DriveIO implements IIO {
    private static DriveIO instance;

    // Drive motors
    private TalonSRX leftDriveMotor;
    private TalonSRX rightDriveMotor;

    private boolean enabled = Constants.DRIVE_ENABLED;

    public static DriveIO getInstance() {
        if(instance == null) {
            instance = new DriveIO();
        }
        return instance;
    }

    private DriveIO() {
        if (!enabled) return;
        initMainMotors();
    }

    private void initMainMotors() {
        this.leftDriveMotor = new TalonSRX(Constants.DRIVE_LEFT_ID);
        this.rightDriveMotor = new TalonSRX(Constants.DRIVE_RIGHT_ID);

        this.leftDriveMotor.setInverted(true);
        this.rightDriveMotor.setInverted(false);
    }

    public void setDriveLeft(double speed) {
        if (!enabled) return;
        this.leftDriveMotor.set(TalonSRXControlMode.PercentOutput, speed);
    }

    public void setDriveRight(double speed) {
        if (!enabled) return;
        this.rightDriveMotor.set(TalonSRXControlMode.PercentOutput, speed);
    }

    @Override
    public void resetInputs() {
        if (!enabled) return;
    }

    @Override
    public void updateInputs() {
        if (!enabled) return;
    }

    @Override
    public void stopAllOutputs() {
        if (!enabled) return;
        this.leftDriveMotor.neutralOutput();
        this.rightDriveMotor.neutralOutput();
    }
}