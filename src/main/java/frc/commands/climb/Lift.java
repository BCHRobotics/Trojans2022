package frc.commands.climb;

import frc.commands.Command;
import frc.robot.Constants;
import frc.subsystems.Climber;

public class Lift extends Command {
    private static Lift instance;

    private Climber climber;

    private double winchPosition;

    public static Lift getInstance() {
        if (instance == null) {
            instance = new Lift();
        }
        return instance;
    }

    private Lift() {
        this.climber = Climber.getInstance();

        setOnEnd(this::onEnd);
    }

    public void setArmHeight(double input) {
        this.winchPosition = input * Constants.CLIMBER_WINCH_ROTATIONS;
        this.calculate();
    }

    public double getArmHeight() {
        return this.winchPosition;
    }

    public boolean armReachedHeight() {
        // double armHeight = this.climber.getWinchPosition() / Constants.CLIMBER_WINCH_ROTATIONS;
        // if (armHeight >= this.winchPosition - 0.05 && armHeight <= this.winchPosition + 0.05) {
        //     return true;
        // } else return false;
        return true;
    }

    @Override
    public void calculate() {
        this.climber.setClimberWinchPosition(this.winchPosition);
    }

    private void onEnd() {
        this.climber.setClimberWinchPosition(0);
    }
    
}
