package frc.commands.intake;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Collect extends Command{
    private static Collect instance;

    private boolean isFinished;

    private Intake intake;

    public static Collect getInstance() {
        if (instance == null) {
            instance = new Collect();
        }
        return instance;
    }

    private Collect() {
        this.intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        this.intake.firstCycle();
        this.isFinished = false;
    }

    @Override
    public void calculate() {
        this.intake.setIntakeState(true);
        this.intake.setIntakeSpeed(1);
    }

    public void reverse() {
        this.intake.setIntakeState(true);
        this.intake.setIntakeSpeed(-1);
    }

    @Override
    public void execute() {
        this.intake.run();
    }

    @Override
    public void end() {
        this.intake.setIntakeState(false);
        this.intake.setIntakeSpeed(0);
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
