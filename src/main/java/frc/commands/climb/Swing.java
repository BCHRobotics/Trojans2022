package frc.commands.climb;

import frc.commands.Command;
import frc.robot.Constants;
import frc.subsystems.Climber;

public class Swing extends Command {
    private static Swing instance;

    private Climber climber;

    private double armPosition;

    private Boolean isFinished;

    public static Swing getInstance() {
        if (instance == null) {
            instance = new Swing();
        }
        return instance;
    }

    private Swing() {
        this.climber = Climber.getInstance();
    }

    @Override
    public void initialize() {
        this.climber.firstCycle();
        this.isFinished = false;
    }

    public void setArmPosition(double input) {
        this.armPosition = input * Constants.ANGLE_LIMIT;
        this.calculate();
    }

    public double getArmPosition() {
        return this.armPosition;
    }

    @Override
    public void calculate() {
        this.climber.setRobotArmPosition(this.armPosition);
    }

    @Override
    public void execute() {
        this.climber.run();
    }

    @Override
    public void end() {
        this.climber.setRobotArmPosition(0);
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.climber.disable();
    }
    
}
