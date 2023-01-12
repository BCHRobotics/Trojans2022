package frc.commands.intake;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Collect extends Command{
    private static Collect instance;

    private Intake intake;

    public static Collect getInstance() {
        if (instance == null) {
            instance = new Collect();
        }
        return instance;
    }

    private Collect() {
        this.intake = Intake.getInstance();

        setOnEnd(this::onEnd);
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

    private void onEnd() {
        this.intake.setIntakeState(false);
        this.intake.setIntakeSpeed(0);
    }
    
}
