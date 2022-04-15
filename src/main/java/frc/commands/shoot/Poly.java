package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Poly extends Command{
    private static Poly instance;

    private boolean isFinished;

    private Intake intake;

    public static Poly getInstance() {
        if (instance == null) {
            instance = new Poly();
        }
        return instance;
    }

    private Poly() {
        this.intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        this.intake.firstCycle();
        this.isFinished = false;
    }

    @Override
    public void calculate() {
        this.intake.setFeederSpeed(1);
    }

    public void reverse() {
        this.intake.setFeederSpeed(-1);
    }

    @Override
    public void execute() {
        this.intake.run();
    }

    @Override
    public void end() {
        this.intake.setFeederSpeed(0);
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
