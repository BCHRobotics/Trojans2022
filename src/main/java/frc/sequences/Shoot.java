package frc.sequences;

import edu.wpi.first.wpilibj.Timer;
import frc.commands.intake.*;
import frc.commands.shoot.*;

public class Shoot extends Sequence{
    private static Shoot instance;

    private boolean isFinished;

    private Timer timer;

    private Collect intakeCommand;
    private Stage stageCommand;
    private Poly feedCommand;
    private Fire launchCommand;
    private Aim limelightCommand;
    private Manual manualShootCommand;

    public static Shoot getInstance() {
        if (instance == null) {
            instance = new Shoot();
        }
        return instance;
    }

    private Shoot() {
        this.intakeCommand = Collect.getInstance();
        this.stageCommand = Stage.getInstance();
        this.feedCommand = Poly.getInstance();
        this.launchCommand = Fire.getInstance();
        this.limelightCommand = Aim.getInstance();
        this.manualShootCommand = Manual.getInstance();

        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        this.intakeCommand.initialize();
        this.stageCommand.initialize();
        this.feedCommand.initialize();
        this.launchCommand.initialize();
        this.limelightCommand.initialize();
        this.manualShootCommand.initialize();
    }

    @Override
    public void startTimer() {
        this.timer.start();
        this.isFinished = false;
    }

    @Override
    public void calculate() {
        if (this.stageCommand.cargoPresent()) {
            this.stageCommand.end();
            this.feedCommand.calculate();

            if (this.stageCommand.colorMatches()) {
                this.limelightCommand.calculate();
            } else {
                this.manualShootCommand.setShooterSpeed(400);
            }

            if (timer.hasElapsed(0.8)) {
                this.launchCommand.calculate();
                if (timer.hasElapsed(1.4)) {
                    this.end();
                }
            } else this.launchCommand.end();
        } else {
            this.stageCommand.calculate();
        }
    }

    @Override
    public void execute() {
        this.intakeCommand.execute();
        this.stageCommand.execute();
        this.feedCommand.execute();
        this.launchCommand.execute();
        this.limelightCommand.execute();
        this.manualShootCommand.execute();
    }

    @Override
    public void end() {
        this.timer.stop();
        this.timer.reset();

        this.isFinished = true;

        this.intakeCommand.end();
        this.stageCommand.end();
        this.feedCommand.end();
        this.launchCommand.end();
        this.limelightCommand.end();
        this.manualShootCommand.end();
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.timer.stop();
        this.timer.reset();

        this.intakeCommand.disable();
        this.stageCommand.disable();
        this.feedCommand.disable();
        this.launchCommand.disable();
        this.limelightCommand.disable();
        this.manualShootCommand.disable();
    }
    
}
