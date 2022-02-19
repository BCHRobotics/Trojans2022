package frc.subsystems;

import frc.io.subsystems.WinchIO;
import frc.io.subsystems.ArmIO;

public class Climber extends Subsystem {
    private static Climber instance;

    private WinchIO winchIO;
    private ArmIO armIO;

    private double climberWinchPosition;
    private double climberArmPosition;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    private Climber() {
        this.winchIO = WinchIO.getInstance();
        this.armIO = ArmIO.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        this.winchIO.setWinchExtension(climberWinchPosition);
        this.armIO.setArmPosition(climberArmPosition);
    }

    @Override
    public void disable() {
        this.winchIO.stopAllOutputs();
        this.armIO.stopAllOutputs();
    }

    /**
     * Set the position of the climber winch
     * 
     * @param position in motor revolutions
     */
    public void setShooterWheelSpeed(double position) {
        this.climberWinchPosition = position;
    }

    /**
     * Set the position of the climber arms
     * 
     * @param position in motor revolutions
     */
    public void setShooterArmPosition(double position) {
        this.climberArmPosition = position;
    }
    
}