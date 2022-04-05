package frc.subsystems;

import frc.io.subsystems.ClimberIO;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.subsystems.ArmIO;

public class Climber extends Subsystem {
    private static Climber instance;

    private ClimberIO winchIO;
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
        this.winchIO = ClimberIO.getInstance();
        this.armIO = ArmIO.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void run() {
        this.winchIO.setWinchExtension(climberWinchPosition);
        this.armIO.setArmPosition(robotArmPosition);
        
        SmartDashboard.putNumber("Winch Encoder", this.winchIO.getLeftWinchEncoder().getPosition());
        SmartDashboard.putNumber("Arm Encoder", this.armIO.getLeftArmEncoder().getPosition());
    }

    @Override
    public void disable() {
        this.winchIO.stopAllOutputs();
        this.armIO.stopAllOutputs();
    }

    public double getArmPosition() {
        return this.armIO.getLeftArmEncoder().getPosition();
    }

    public double getWinchPosition() {
        return this.winchIO.getLeftWinchEncoder().getPosition();
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