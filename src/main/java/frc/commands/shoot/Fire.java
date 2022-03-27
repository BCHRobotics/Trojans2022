package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Fire extends Command{
    private static Fire instance;

    private boolean isFinished;

    private Intake intake;

    public static Fire getInstance() {
        if (instance == null) {
            instance = new Fire();
        }
        return instance;
    }

    private Fire() {
        this.intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        this.intake.firstCycle();
        this.isFinished = false;
    }

    @Override
    public void calculate() {
        this.intake.setFeederState(true);
    }

    @Override
    public void execute() {
        this.intake.run();
    }

    @Override
    public void end() {
        this.intake.setFeederState(false);
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.intake.disable();
    }
    
}
