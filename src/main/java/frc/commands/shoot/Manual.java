package frc.commands.shoot;

import frc.commands.Command;
import frc.subsystems.Shooter;

public class Manual extends Command {
    private static Manual instance;

    private Shooter shooter;

    private double speed;

    public static Manual getInstance() {
        if (instance == null) {
            instance = new Manual();
        }
        return instance;
    }

    private Manual() {
        this.shooter = Shooter.getInstance();

        setOnEnd(this::onEnd);
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

    private void onEnd() {
        this.shooter.setShooterWheelSpeed(0);
        this.calculate();
        this.isFinished = true;
    }
    
}
