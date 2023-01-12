package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Fire extends Command{
    private static Fire instance;
    private Intake intake;

    public static Fire getInstance() {
        if (instance == null) {
            instance = new Fire();
        }
        return instance;
    }

    private Fire() {
        this.intake = Intake.getInstance();

        setOnEnd(this::onEnd);
    }

    @Override
    public void calculate() {
        this.intake.setFeederState(true);
    }

    private void onEnd() {
        this.intake.setFeederState(false);
        this.isFinished = true;
    }

}
