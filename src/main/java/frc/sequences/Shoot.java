package frc.sequences;

import edu.wpi.first.wpilibj.Timer;
import frc.commands.shoot.*;

public class Shoot extends Sequence{
    private static Shoot instance;

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
        // TODO Auto-generated method stub
        this.timer.start();
    }

    @Override
    public void calculate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
        this.timer.stop();
        this.timer.reset();
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        this.timer.stop();
        this.timer.reset();
    }
    
}
