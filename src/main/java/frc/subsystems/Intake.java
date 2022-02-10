package frc.subsystems;

import frc.io.subsystems.IntakeIO;

public class Intake extends Subsystem {
    private static Intake instance;
    
    private IntakeIO intakeIO;
   
    private double rollerSpeed;
    private boolean intakeUnfolded;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }



    public Intake() {
        this.intakeIO = IntakeIO.getInstance();
        
        this.firstCycle();
    }

    public void setRollerSpeed(double speed) {
        if (speed > 1) speed = 1;
        else if (speed < -1) speed = -1;

        this.rollerSpeed = speed;
    }

    public void setUnfoldIntake(boolean unfold) {
        this.intakeUnfolded = unfold;
    }

    @Override
    public void firstCycle() {
    
    }

    @Override
    public void calculate() {
        this.intakeIO.setRollerSpeed(this.rollerSpeed);
        this.intakeIO.setUnfoldIntake(this.intakeUnfolded);
        
    }

    @Override
    public void disable() {
        this.intakeIO.setRollerSpeed(0);
        this.intakeIO.setUnfoldIntake(false);
    }

        
}