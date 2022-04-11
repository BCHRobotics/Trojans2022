package frc.commands.shoot;

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
        if (this.colorMatches) return this.shooter.getAverageWheelSpeed() >= this.shooterSpeed - 600;
        else return this.shooter.getAverageWheelSpeed() >= this.shooterSpeed - 30;
    }

    public boolean limelightLatched() {
        return this.limelight.getTargetX() >= -0.15 && this.limelight.getTargetX() <= 0.15;
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
