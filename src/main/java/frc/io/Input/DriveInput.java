package frc.io.Input;

import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.SerialPort;

import frc.io.Output.RobotOutput;
import frc.robot.Constants;
import frc.util.Navx;

public class DriveInput implements ISensorInput {
    private static DriveInput instance;

    private RobotOutput robotOutput;

    // Gyro
    private Navx navx;
    
    // Drive encoders
    private CANEncoder driveL1Encoder;
    private CANEncoder driveL2Encoder;
    private CANEncoder driveR1Encoder;
    private CANEncoder driveR2Encoder;

    // Robot position
    private double xPosition = 0;
    private double yPosition = 0;

    // Timing
    private double lastTime = 0.0;
	private double deltaTime = 20.0;

    public static DriveInput getInstance() {
        if (instance == null) {
            instance = new DriveInput();
        }
        return instance;
    }

    private DriveInput() {
        this.robotOutput = RobotOutput.getInstance();

        this.navx = new Navx(SerialPort.Port.kUSB);

        this.driveL1Encoder = robotOutput.drive.getDriveL1Encoder();
        this.driveL2Encoder = robotOutput.drive.getDriveL2Encoder();
        this.driveR1Encoder = robotOutput.drive.getDriveR1Encoder();
        this.driveR2Encoder = robotOutput.drive.getDriveR2Encoder();

        // when factor 1 travels: 46.4
        double driveFactor = 100 / 46.4;
        this.driveL1Encoder.setPositionConversionFactor(driveFactor);
        this.driveL2Encoder.setPositionConversionFactor(driveFactor);
        this.driveR1Encoder.setPositionConversionFactor(driveFactor);
        this.driveR2Encoder.setPositionConversionFactor(driveFactor);
    }

    @Override
    public void reset() {
        this.navx.reset();

        this.driveL1Encoder.setPosition(0);
        this.driveL2Encoder.setPosition(0);
        this.driveR1Encoder.setPosition(0);
        this.driveR2Encoder.setPosition(0);

        this.xPosition = 0;
        this.yPosition = 0;
    }

    @Override
    public void update() {

        if (this.lastTime == 0.0) {
            this.deltaTime = 20;
            this.lastTime = System.currentTimeMillis();
        } else {
            this.deltaTime = System.currentTimeMillis() - lastTime;
            this.lastTime = System.currentTimeMillis();
        }

        this.navx.update();

        double driveXSpeed = getDriveSpeedFPS() * Math.cos(Math.toRadians(getGyroAngle()));
        double driveYSpeed = getDriveSpeedFPS() * Math.sin(Math.toRadians(getGyroAngle()));
        xPosition += driveXSpeed * this.deltaTime / 1000.0;
        yPosition += driveYSpeed * this.deltaTime / 1000.0;
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


    //#region DrivePosition

    public void setDriveXPos(double x) {
        this.xPosition = x;
    }

    public void setDriveYPos(double y) {
        this.yPosition = y;
    }

    public double getDriveXPos() {
        return this.xPosition;
    }

    public double getDriveYPos() {
        return this.yPosition;
    }

    //#endregion DrivePosition


    //#region EncoderFPS

    public double getDriveL1SpeedFPS() {
        return (this.driveL1Encoder.getVelocity() * (Constants.WhHEEL_DIAMETER * Math.PI) / 12) / 60;
    }

    public double getDriveL2SpeedFPS() {
        return (this.driveL2Encoder.getVelocity() * (Constants.WhHEEL_DIAMETER * Math.PI) / 12) / 60;
    }

    public double getDriveR1SpeedFPS() {
        return (this.driveR1Encoder.getVelocity() * (Constants.WhHEEL_DIAMETER * Math.PI) / 12) / 60;
    }

    public double getDriveR2SpeedFPS() {
        return (this.driveR2Encoder.getVelocity() * (Constants.WhHEEL_DIAMETER * Math.PI) / 12) / 60;
    }

    public double getDriveLeftSpeedFPS() {
        return (getDriveL1SpeedFPS() + getDriveL2SpeedFPS()) / 2.0;
    }

    public double getDriveRightSpeedFPS() {
        return (getDriveR1SpeedFPS() + getDriveR2SpeedFPS()) / 2.0;
    }

    public double getDriveSpeedFPS() {
        return (getDriveLeftSpeedFPS() + getDriveRightSpeedFPS()) / 2.0;
    }

    //#endregion EncoderFPS
    

    //#region Gyro

    public double getGyroAngle() {
        return this.navx.getAngle();
    }

    //#endregion Gyro
}
