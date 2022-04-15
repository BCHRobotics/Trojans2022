package frc.commands.shoot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.Command;
import frc.subsystems.Drivetrain;
import frc.subsystems.Shooter;
import frc.util.imaging.Limelight;
import frc.util.imaging.Limelight.LimelightTargetType;

public class Aim extends Command {
    private static Aim instance;

    private Drivetrain drive;
    private Shooter shooter;
    private Limelight limelight;

    private boolean isFinished;
    private boolean colorMatches;
    private double shooterSpeed;

    private final double aValue = -8.28e-6;
    private final double bValue = 1.02;
    private final double cValue = -67.6;
    private double outputValue;


    public static Aim getInstance() {
        if (instance == null) {
            instance = new Aim();
        }
        return instance;
    }

    private Aim() {
        this.drive = Drivetrain.getInstance();
        this.shooter = Shooter.getInstance();
        this.limelight = Limelight.getInstance();
    }

    @Override
    public void initialize() {
        this.drive.firstCycle();
        this.shooter.firstCycle();
        this.limelight.setLedMode(1);
        this.limelight.setLimelightState(LimelightTargetType.UPPER_HUB);

        this.isFinished = false;
    }

    @Override
    public void calculate() {
        this.limelight.setLedMode(3);
        this.drive.setPositionMode(true);
        this.drive.brake(true);
        this.drive.seekTarget(this.limelight.getTargetX());
        if (this.colorMatches) this.shooterSpeed = this.shooter.calculateShooterRPM(this.limelight.getTargetDistance());
        else this.shooterSpeed = 600;
        this.shooter.setShooterWheelSpeed(this.shooterSpeed);
        SmartDashboard.putNumber("Setpoint", this.shooter.calculateShooterRPM(this.limelight.getTargetDistance()));
    }

    @Override
    public void execute() {
        this.shooter.run();
        this.drive.run();
    }

    @Override
    public void end() {
        this.limelight.setLedMode(1);
        this.drive.setPositionMode(false);
        this.shooterSpeed = 0;
        this.shooter.setShooterWheelSpeed(0);
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    public boolean reachedSpeed() {
        if (this.colorMatches) {
            this.outputValue = (this.aValue * Math.pow(this.shooterSpeed, 2)) + (this.bValue * (this.shooterSpeed)) + (this.cValue);
            return this.shooter.getAverageWheelSpeed() >= (this.outputValue - 20);
        }
        else return this.shooter.getAverageWheelSpeed() >= 500;
    }

    public boolean limelightLatched() {
        return ((this.limelight.getTargetX() >= -0.8) && (this.limelight.getTargetX() <= 0.8));
    }

    public void setColorMatch(boolean state) {
        this.colorMatches = state;
    }

    public boolean getColorMatch() {
        return this.colorMatches;
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
    }
    
}
