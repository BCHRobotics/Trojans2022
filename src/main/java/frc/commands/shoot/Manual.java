package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Shooter;

public class Manual extends Command {
    private static Manual instance;

    private Shooter shooter;

    private double speed;

    private Boolean isFinished;

    public static Manual getInstance() {
        if (instance == null) {
            instance = new Manual();
        }
        return instance;
    }

    private Manual() {
        this.shooter = Shooter.getInstance();
    }

    @Override
    public void initialize() {
        this.shooter.firstCycle();
        this.isFinished = false;
    }

    public void setShooterSpeed(double input) {
        this.speed = input;
        this.calculate();
    }

    public double getShooterSpeed() {
        return this.speed;
    }

    @Override
    public void calculate() {
        this.shooter.setShooterWheelSpeed(this.speed);
    }

    @Override
    public void execute() {
        this.shooter.run();
    }

    @Override
    public void end() {
        this.shooter.setShooterWheelSpeed(0);
        this.calculate();
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.shooter.disable();
    }
    
}
