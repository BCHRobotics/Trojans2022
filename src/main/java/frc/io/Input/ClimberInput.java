package frc.io.Input;

import com.revrobotics.CANEncoder;

import frc.io.Output.RobotOutput;

public class ClimberInput implements ISensorInput {
    private RobotOutput robotOutput;

    private CANEncoder armExtendEncoder;
    private CANEncoder armRotateEncoder;

    protected ClimberInput() {
        this.robotOutput = RobotOutput.getInstance();

        this.armExtendEncoder = robotOutput.climber.getArmExtendEncoder();
        this.armRotateEncoder = robotOutput.climber.getArmRotateEncoder();

        this.armExtendEncoder.setPositionConversionFactor(1);
        this.armRotateEncoder.setPositionConversionFactor(1);
    }

    @Override
    public void reset() {
        this.armExtendEncoder.setPosition(0);
        this.armRotateEncoder.setPosition(0);
    }

    @Override
    public void update() {

    }

    public double getArmExtendEncoder() {
        return this.armExtendEncoder.getPosition();
    }

    public double getArmRotateEncoder() {
        return this.armRotateEncoder.getPosition();
    }

}
