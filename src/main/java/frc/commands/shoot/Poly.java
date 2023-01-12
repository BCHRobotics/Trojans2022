package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Intake;

public class Poly extends Command{
    private static Poly instance;
    private Intake intake;

    public static Poly getInstance() {
        if (instance == null) {
            instance = new Poly();
        }
        return instance;
    }

    private Poly() {
        this.intake = Intake.getInstance();

        setOnEnd(this::onEnd);
    }

    @Override
    public void calculate() {
        this.intake.setFeederSpeed(1);
    }

    public void reverse() {
        this.intake.setFeederSpeed(-1);
    }

    private void onEnd() {
        this.intake.setFeederSpeed(0);
        this.isFinished = true;
    }
    
}
