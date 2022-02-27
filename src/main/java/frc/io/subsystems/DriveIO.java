package frc.io.subsystems;

// Import required Libraries
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
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
    private CANSparkMax driveL2;
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

    private boolean enabled = Constants.DRIVE_ENABLED;
    private boolean miniBot = Constants.MINI_BOT;

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

        if (!miniBot) initFollowMotors();
    }

    private void initMainMotors() {
        this.driveL1 = new CANSparkMax(Constants.DRIVE_LEFT1_ID, MotorType.kBrushless); 
        this.driveR1 = new CANSparkMax(Constants.DRIVE_RIGHT1_ID, MotorType.kBrushless);

        this.driveL1Encoder = driveL1.getEncoder();
        this.driveR1Encoder = driveR1.getEncoder();

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

    private void initFollowMotors() {
        this.driveL2 = new CANSparkMax(Constants.DRIVE_LEFT2_ID, MotorType.kBrushless);  
        this.driveR2 = new CANSparkMax(Constants.DRIVE_RIGHT2_ID, MotorType.kBrushless);
        
        this.driveL2Encoder = driveL2.getEncoder();
        this.driveR2Encoder = driveR2.getEncoder();
        
        this.driveL2.restoreFactoryDefaults();
        this.driveR2.restoreFactoryDefaults();

        this.driveL2.setIdleMode(this.driveL1.getIdleMode());
        this.driveR2.setIdleMode(this.driveR1.getIdleMode());

        this.driveL2.setSmartCurrentLimit(60, 10);
        this.driveR2.setSmartCurrentLimit(60, 10);

        this.driveL2.follow(this.driveL1, false);
        this.driveR2.follow(this.driveR1, false);
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

    public void brakeMode() {
        if (SmartDashboard.getBoolean("Brake Mode", false)) {
            this.driveL1.setIdleMode(CANSparkMax.IdleMode.kBrake);
            this.driveR1.setIdleMode(CANSparkMax.IdleMode.kBrake);
        } else {
            this.driveL1.setIdleMode(CANSparkMax.IdleMode.kCoast);
            this.driveR1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        }
    }

    //#region EncoderPositions

    public RelativeEncoder getDriveL1Encoder() {
        if (!enabled) return null;
        return this.driveL1Encoder;
    }

    public RelativeEncoder getDriveL2Encoder() {
        if (!enabled || !miniBot) return null;
        return this.driveL2Encoder;
    }

    public RelativeEncoder getDriveR1Encoder() {
        if (!enabled) return null;
        return this.driveR1Encoder;
    }

    public RelativeEncoder getDriveR2Encoder() {
        if (!enabled || !miniBot) return null;
        return this.driveR2Encoder;
    }

    //#endregion EncoderPositions

    @Override
    public void resetInputs() {
        if (!enabled) return;

        this.driveL1Encoder.setPosition(0);
        this.driveR1Encoder.setPosition(0);

        if (!miniBot) {
            this.driveL2Encoder.setPosition(0);
            this.driveR2Encoder.setPosition(0);
        }
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

        if (!miniBot) {
            this.driveL2.disable();
            this.driveR2.disable();
        }
    }
}