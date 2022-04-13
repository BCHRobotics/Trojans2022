package frc.sequences;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.intake.*;
import frc.commands.shoot.*;

public class Shoot extends Sequence{
    private static Shoot instance;

    private boolean isFinished;
    private boolean verified;

    private Timer timer;

    private Stage stageCommand;
    private Poly feedCommand;
    private Fire launchCommand;
    private Aim limelightCommand;
    // private Manual manualShootCommand;

    private boolean shotLatch;

    public static Shoot getInstance() {
        if (instance == null) {
            instance = new Shoot();
        }
        return instance;
    }

    private Shoot() {
        this.stageCommand = Stage.getInstance();
        this.feedCommand = Poly.getInstance();
        this.launchCommand = Fire.getInstance();
        this.limelightCommand = Aim.getInstance();
        // this.manualShootCommand = Manual.getInstance();

        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        this.stageCommand.initialize();
        this.feedCommand.initialize();
        this.launchCommand.initialize();
        this.limelightCommand.initialize();
        // this.manualShootCommand.initialize();
    }

    private void startTimer() {
        this.timer.stop();
        this.timer.reset();
        this.timer.start();
        this.isFinished = false;
        this.verified = true;
    }

    @Override
    public void calculate() {
        if (this.stageCommand.cargoPresent()) {
            this.shotLatch = true;

            this.stageCommand.end();
            this.feedCommand.calculate();

            if (this.stageCommand.colorMatches()) this.limelightCommand.setColorMatch(true);
            else this.limelightCommand.setColorMatch(false);

            this.limelightCommand.calculate();

            // (this.limelightCommand.limelightLatched() || !this.limelightCommand.getColorMatch())
            if (((this.limelightCommand.limelightLatched() && this.limelightCommand.reachedSpeed())|| !this.limelightCommand.getColorMatch()) && !this.verified) this.startTimer();
            
            SmartDashboard.putBoolean("Aimed", (((this.limelightCommand.limelightLatched() && this.limelightCommand.reachedSpeed()) || !this.limelightCommand.getColorMatch())) && this.verified);
            SmartDashboard.putBoolean("Lime lock", this.limelightCommand.limelightLatched());
            
            if (this.timer.hasElapsed(0.8)) this.launchCommand.calculate();
            else this.launchCommand.end();

        } else if (!this.shotLatch) {
            this.stageCommand.calculate();
            this.limelightCommand.calculate();
        }

        if (this.timer.hasElapsed(1.6) && this.shotLatch) {
            this.end();
        }
    }

    @Override
    public void execute() {
        this.stageCommand.execute();
        this.feedCommand.execute();
        this.launchCommand.execute();
        this.limelightCommand.execute();
        // this.manualShootCommand.execute();
    }

    @Override
    public void end() {
        this.timer.stop();
        this.timer.reset();

        this.stageCommand.end();
        this.feedCommand.end();
        this.launchCommand.end();
        this.limelightCommand.end();
        // this.manualShootCommand.end();

        this.shotLatch = false;
        this.verified = false;
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    public void reset() {
        this.isFinished = false;
    }

    @Override
    public void disable() {
        this.timer.stop();
        this.timer.reset();

        this.stageCommand.disable();
        this.feedCommand.disable();
        this.launchCommand.disable();
        this.limelightCommand.disable();
        // this.manualShootCommand.disable();
    }
    
}
