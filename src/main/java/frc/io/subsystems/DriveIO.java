package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// Import required Classes
import frc.robot.Constants;
import frc.util.pid.SparkMaxConstants;
import frc.util.pid.SparkMaxPID;

public class DriveIO implements IIO {
    private static DriveIO instance;

    // Drive motors
    private CANSparkMax driveL1;
    private CANSparkMax driveR1;

    // PID Controllers
    private SparkMaxPID driveL1PidController;
    private SparkMaxPID driveR1PidController;

    // PID Constants
    private SparkMaxConstants driveL1Constants = Constants.DRIVEL1_CONSTANTS;
    private SparkMaxConstants driveR1Constants = Constants.DRIVER1_CONSTANTS;

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
        SmartDashboard.putBoolean("Brake Mode", false);
    }

    private void initMainMotors() {
        this.driveL1 = new CANSparkMax(Constants.DRIVE_LEFT1_ID, MotorType.kBrushed); 
        this.driveR1 = new CANSparkMax(Constants.DRIVE_RIGHT1_ID, MotorType.kBrushed);

        this.driveL1.restoreFactoryDefaults();
        this.driveR1.restoreFactoryDefaults();

        this.driveL1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        this.driveR1.setIdleMode(CANSparkMax.IdleMode.kCoast);

        this.driveL1.setSmartCurrentLimit(60, 10);
        this.driveR1.setSmartCurrentLimit(60, 10);

        this.driveL1PidController = new SparkMaxPID(driveL1, driveL1Constants);
        this.driveR1PidController = new SparkMaxPID(driveR1, driveR1Constants);

        this.driveL1.setInverted(true);
        this.driveR1.setInverted(false);

        System.out.println(driveL1PidController.getConstants());
        System.out.println(driveR1PidController.getConstants());
    }

    public void setDriveLeft(double speed) {
        if (!enabled) return;
        this.driveL1.set(speed);
    }

    public void setDriveRight(double speed) {
        if (!enabled) return;
        this.driveR1.set(speed);
    }

    public void setDriveLeftPos(double position) {
        if (!enabled) return;
        this.driveL1PidController.setPosition(position);
    }

    public void setDriveRightPos(double position) {
        if (!enabled) return;
        this.driveR1PidController.setPosition(position);
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
        this.driveL1.disable();
        this.driveR1.disable();
    }
}