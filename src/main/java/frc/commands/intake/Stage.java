package frc.commands.intake;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Stage extends Command{
    private static Stage instance;

    private boolean isFinished;

    private Intake intake;

    public static Stage getInstance() {
        if (instance == null) {
            instance = new Stage();
        }
        return instance;
    }

    private Stage() {
        this.intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        this.intake.firstCycle();
        this.isFinished = false;
    }

    @Override
    public void calculate() {
        this.intake.setStagerSpeed(1);
    }

    public void reverse() {
        this.intake.setStagerSpeed(-1);
    }

    @Override
    public void execute() {
        this.intake.run();
    }

    @Override
    public void end() {
        this.intake.setStagerSpeed(0);
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
