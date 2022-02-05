package frc.subsystems;

import frc.io.subsystems.ShooterIO;

public class Shooter extends Subsystem {
    private static Shooter instance;

    private ShooterIO shooterIO;

    private double shooterWheelSpeed;
    private double shooterTurretPosition;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    private Shooter() {
        this.shooterIO = ShooterIO.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        this.shooterIO.setWheelSpeed(shooterWheelSpeed);
        this.shooterIO.setTurretPosition(shooterTurretPosition);
    }

    @Override
    public void disable() {
        // Auto-generated method stub
        this.shooterIO.stopAllOutputs();
    }

    public void setShooterWheelSpeed(double speed) {
        this.shooterWheelSpeed = speed;
    }

    public void setShooterTurretPosition(double position) {
        this.shooterTurretPosition = position;
    }
    
}