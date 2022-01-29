package frc.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.Input.SensorInput;
import frc.io.Output.RobotOutput;

public class Climber extends Subsystem {
    private static Climber instance;

    private RobotOutput robotOutput;
    private SensorInput sensorInput;

    public enum ClimberMode {
        INDEPENDENT,
        SYNCHRONIZED
    }

    private ClimberMode currentMode = ClimberMode.SYNCHRONIZED;

    private double rightExtendOutput;
    private double leftExtendOutput;
    private double rightRotateOutput;
    private double leftRotateOutput;

    private double syncExtendOutput;
    private double syncRotateOutput;

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    private Climber() {
        this.robotOutput = RobotOutput.getInstance();
        this.sensorInput = SensorInput.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        SmartDashboard.putString("ClimberMode", this.currentMode.toString());

        switch (currentMode) {
            case INDEPENDENT:
                this.robotOutput.climber.setArmExtendRight(rightExtendOutput);
                this.robotOutput.climber.setArmExtendLeft(leftExtendOutput);

                this.robotOutput.climber.setArmRotateRight(rightRotateOutput);
                this.robotOutput.climber.setArmRotateLeft(leftRotateOutput);
                break;
            case SYNCHRONIZED:
            default:
                this.robotOutput.climber.setArmExtend(syncExtendOutput);
                this.robotOutput.climber.setArmRotate(syncRotateOutput);
                break;
        }
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        
    }

    public ClimberMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(ClimberMode currentMode) {
        this.currentMode = currentMode;
    }

    public void setRightExtendOutput(double rightExtendOutput) {
        this.rightExtendOutput = rightExtendOutput;
    }

    public void setLeftExtendOutput(double leftExtendOutput) {
        this.leftExtendOutput = leftExtendOutput;
    }

    public void setRightRotateOutput(double rightRotateOutput) {
        this.rightRotateOutput = rightRotateOutput;
    }

    public void setLeftRotateOutput(double leftRotateOutput) {
        this.leftRotateOutput = leftRotateOutput;
    }

    public void setSyncExtendOutput(double syncExtendOutput) {
        this.syncExtendOutput = syncExtendOutput;
    }

    public void setSyncRotateOutput(double syncRotateOutput) {
        this.syncRotateOutput = syncRotateOutput;
    }

    
    
}
