package frc.commands;

import frc.subsystems.Drive;
import frc.subsystems.Shooter;
import frc.util.imaging.Limelight;
import frc.util.imaging.Limelight.LimelightTargetType;

public class Aim extends Command {

    private Drive drive;
    private Shooter shooter;
    private Limelight limelight;

    private Boolean isFinished;

    public Aim() {
        this.drive = Drive.getInstance();
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
    public void runCycle() {
        this.limelight.setLedMode(3);
        this.drive.setPositionMode(true);
        this.drive.brake(true);
        this.drive.seekTarget(this.limelight.getTargetX());
        this.shooter.setShooterWheelSpeed(this.shooter.calculateShooterRPM(this.limelight.getTargetDistance()));

        this.execute();
    }

    @Override
    protected void execute() {
        this.shooter.calculate();
        this.drive.calculate();
    }

    @Override
    public void end() {
        this.limelight.setLedMode(1);
        this.drive.setPositionMode(false);
        this.drive.brake(false);
        this.shooter.setShooterWheelSpeed(0);

        this.isFinished = true;

        this.execute();
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.shooter.disable();
    }
    
}
