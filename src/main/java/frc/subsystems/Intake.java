package frc.subsystems;

import frc.io.subsystems.IntakeIO;

public class Intake extends Subsystem {
    private static Intake instance;

    private IntakeIO intakeIO;

    private double intakeSpeed;
    private double stagerSpeed;
    private double feederSpeed;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private Intake() {
        this.intakeIO = IntakeIO.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        this.intakeIO.setIntakeSpeed(intakeSpeed);
        this.intakeIO.setStagerSpeed(stagerSpeed);
        this.intakeIO.setFeederSpeed(feederSpeed);
    }

    @Override
    public void disable() {
        this.intakeIO.stopAllOutputs();
    }

    /**
     * Set the speed of the intake
     * 
     * @param speed in decimal
     */
    public void setIntakeSpeed(double speed) {
        this.intakeSpeed = speed;
    }

    /**
     * Set the speed of the stager
     * 
     * @param speed in decimal
     */
    public void setStagerSpeed(double speed) {
        this.stagerSpeed = speed;
    }

    /**
     * Set the speed of the feeder
     * 
     * @param speed in decimal
     */
    public void setFeederSpeed(double speed) {
        this.feederSpeed = speed;
    }
}