package frc.subsystems;

import frc.io.subsystems.IntakeIO;

public class Intake extends Subsystem {
    private static Intake instance;

    private IntakeIO intakeIO;

    private double intakeSpeed;
    private double stagerSpeed;
    private double feederSpeed;
    private boolean intakeState;
    private boolean feederState;

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
        this.intakeIO.setIntakeState(intakeState);
        this.intakeIO.setFeederArmState(feederState);
    }

    @Override
    public void disable() {
        this.intakeIO.stopAllOutputs();
    }

    public void resetPosition() {
        this.intakeIO.getIntakeEncoder().setPosition(0);
        this.intakeIO.getStagerEncoder().setPosition(0);
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

    /**
     * Set the position of the intake
     * 
     * @param state as a boolean value
     * { FALSE: Raised | TRUE: Lowered }
     */
    public void setIntakeState(boolean state) {
        this.intakeState = state;
    }

    /**
     * Set the position of the feeder
     * 
     * @param state as a boolean value
     * { FALSE: Raised | TRUE: Lowered }
     */
    public void setFeederState(boolean state) {
        this.feederState = state;
    }
}