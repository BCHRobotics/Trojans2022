package frc.commands.climb;

import frc.commands.Command;
import frc.robot.Constants;
import frc.subsystems.Climber;

public class Swing extends Command {
    private static Swing instance;

    private Climber climber;

    private double armPosition;

    public static Swing getInstance() {
        if (instance == null) {
            instance = new Swing();
        }
        return instance;
    }

    private Swing() {
        this.climber = Climber.getInstance();

        setOnEnd(this::onEnd);
    }

    public void setArmPosition(double input) {
        this.armPosition = input * Constants.ANGLE_LIMIT;
        this.calculate();
    }

    public double getArmPosition() {
        return this.armPosition;
    }

    public boolean armReachedPosition() {
        // double armPosition = this.climber.getArmPosition() / Constants.ANGLE_LIMIT;
        // if (armPosition >= this.armPosition - 0.02 && armPosition <= this.armPosition + 0.02) {
        //     return true;
        // } else return false;
        return true;
    }

    @Override
    public void calculate() {
        this.climber.setRobotArmPosition(this.armPosition);
    }

    private void onEnd() {
        this.climber.setRobotArmPosition(0);
    }
    
}
