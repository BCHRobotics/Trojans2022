package frc.subsystems;

import frc.io.subsystems.IntakeIO;

/**
 * This class initiates the Intake subsystem
 * @author  Tim Rostorhuiev
 * @author  Ryan Moffatt 
 */
public class Intake extends Subsystem {
    private static Intake instance;
    
    private IntakeIO intakeIO;
   
    private double rollerSpeed;
    private boolean intakeUnfolded;

    /**
     * Making the IntakeIO a singleton instance
     * @return Singleton
     */
    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    /**
     * Initializes all the variables
     */
    public Intake() {
        this.intakeIO = IntakeIO.getInstance();
        
        this.firstCycle();
    }

    /**
     * Set the roller speed
     * @param speed
     */
    public void setRollerSpeed(double speed) {
        if (speed > 1) speed = 1;
        else if (speed < -1) speed = -1;

        this.rollerSpeed = speed;
    }

    /**
     * Set the pneumatics for the intake On/Off 
     * @param on
     */
    public void setUnfoldIntake(boolean unfold) {
        this.intakeUnfolded = unfold;
    }

    /**
     * Runs the first cycle
     */
    @Override
    public void firstCycle() {
    
    }

    /**
     * Calculates the speed of the motor and pneumatic
     */
    @Override
    public void calculate() {
        this.intakeIO.setRollerSpeed(this.rollerSpeed);
        this.intakeIO.setUnfoldIntake(this.intakeUnfolded);
        
    }

    /**
     * Disables the motor and pneumatic
     */
    @Override
    public void disable() {
        this.intakeIO.setRollerSpeed(0);
        this.intakeIO.setUnfoldIntake(false);
    }
   
}
