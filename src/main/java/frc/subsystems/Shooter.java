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
    public void run() {
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

    public double getAverageWheelSpeed() {
        return (this.shooterIO.getLeftWheelEncoder().getVelocity() + this.shooterIO.getRightWheelEncoder().getVelocity()) / 2;
    }

    /**
     * Calculates the necessary shooter RPM value from a given distance to hit the target
     * @param setDistance distance in meters
     * @return
     */
    public double calculateShooterRPM(double setDistance) {
        final double a = 23.1;
        final double b = 26.8;
        final double c = 1616;
        double output  = (a*Math.pow(setDistance, 2)) + (b*setDistance) + c;
        // double output = SmartDashboard.getNumber("Shooter Wheels", 0);
        return output;
    }

}