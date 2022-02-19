package frc.subsystems;

import frc.io.subsystems.WinchIO;
import frc.io.subsystems.ArmIO;

public class Climber extends Subsystem {
    private static Climber instance;

    private WinchIO winchIO;
    private ArmIO armIO;

    private double climberWinchPosition;
    private double robotArmPosition;

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
        this.armIO.setArmPosition(robotArmPosition);
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
    public void setClimberWinchPosition(double position) {
        this.climberWinchPosition = position;
    }

    /**
     * Set the position of the robot arms
     * 
     * @param position in motor revolutions
     */
    public void setRobotArmPosition(double position) {
        this.robotArmPosition = position;
    }
}