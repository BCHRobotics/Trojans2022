package frc.subsystems;

import frc.io.subsystems.ShooterIO;
import frc.io.subsystems.ArmIO;

public class Shooter extends Subsystem {
    private static Shooter instance;

    private ShooterIO shooterIO;
    private ArmIO armIO;

    private double shooterWheelSpeed;
    private double shooterArmPosition;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    private Shooter() {
        this.shooterIO = ShooterIO.getInstance();
        this.armIO = ArmIO.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        this.shooterIO.setWheelSpeed(shooterWheelSpeed);
        this.armIO.setArmPosition(shooterArmPosition);
    }

    @Override
    public void disable() {
        this.shooterIO.stopAllOutputs();
        this.armIO.stopAllOutputs();
    }

    /**
     * Set the speed of the shooter wheels
     * 
     * @param speed in rpm
     */
    public void setShooterWheelSpeed(double speed) {
        this.shooterWheelSpeed = speed;
    }

    /**
     * Set the position of the shooter arms
     * 
     * @param position in motor revolutions
     */
    public void setShooterArmPosition(double position) {
        this.shooterArmPosition = position;
    }
    
}