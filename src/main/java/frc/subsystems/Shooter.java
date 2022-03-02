package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.subsystems.ShooterIO;

public class Shooter extends Subsystem {
    private static Shooter instance;

    private ShooterIO shooterIO;

    private double shooterWheelSpeed;

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
        SmartDashboard.putNumber("SHOOTER LEFT ENCODER", this.shooterIO.getLeftWheelEncoder().getVelocity());
        SmartDashboard.putNumber("SHOOTER RIGHT ENCODER", this.shooterIO.getRightWheelEncoder().getVelocity());
    }

    @Override
    public void disable() {
        this.shooterIO.stopAllOutputs();
    }

    /**
     * Set the speed of the shooter wheels
     * 
     * @param speed in rpm
     */
    public void setShooterWheelSpeed(double speed) {
        this.shooterWheelSpeed = speed;
    }

}