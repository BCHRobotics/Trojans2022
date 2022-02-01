package frc.io.Input;

import com.revrobotics.RelativeEncoder;

import frc.io.Output.RobotOutput;

public class ClimberInput implements ISensorInput {
    private static ClimberInput instance;

    private RobotOutput robotOutput;

    private RelativeEncoder armExtendRightEncoder;
    private RelativeEncoder armExtendLeftEncoder;

    private RelativeEncoder armRotateRightEncoder;
    private RelativeEncoder armRotateLeftEncoder;

    public static ClimberInput getInstance() {
        if (instance == null) {
            instance = new ClimberInput();
        }
        return instance;
    }

    /**
     * Initiates the Climber Input, gets the encoders from RobotOutput and assigns them
     */
    private ClimberInput() {
        this.robotOutput = RobotOutput.getInstance();

        this.armExtendRightEncoder = robotOutput.climber.getArmExtendRightEncoder();
        this.armExtendLeftEncoder = robotOutput.climber.getArmExtendLeftEncoder();

        this.armRotateRightEncoder = robotOutput.climber.getArmRotateRightEncoder();
        this.armRotateLeftEncoder = robotOutput.climber.getArmRotateLeftEncoder();

        // TODO: Change position conversion factor for physical robot
        this.armExtendRightEncoder.setPositionConversionFactor(1);
        this.armExtendLeftEncoder.setPositionConversionFactor(1);

        this.armRotateRightEncoder.setPositionConversionFactor(1);
        this.armRotateLeftEncoder.setPositionConversionFactor(1);
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void reset() {
        this.armExtendRightEncoder.setPosition(0);
        this.armExtendLeftEncoder.setPosition(0);

        this.armRotateRightEncoder.setPosition(0);
        this.armRotateLeftEncoder.setPosition(0);
    }

    /**
     * Update the state of the inputs
     */
    @Override
    public void update() {

    }

    /**
     * Get the position of the Right Arm Extend Encoder
     * @return position in inches
     */
    public double getArmExtendRightEncoder() {
        return this.armExtendRightEncoder.getPosition();
    }

    /**
     * Get the position of the Left Arm Extend Encoder
     * @return position in inches
     */
    public double getArmExtendLeftEncoder() {
        return this.armExtendLeftEncoder.getPosition();
    }

    /**
     * Get the position of the Right Arm Rotate Encoder
     * @return position in degrees
     */
    public double getArmRotateRightEncoder() {
        return this.armRotateRightEncoder.getPosition();
    }

    /**
     * Get the position of the Left Arm Rotate Encoder
     * @return position in degrees
     */
    public double getArmRotateLeftEncoder() {
        return this.armRotateLeftEncoder.getPosition();
    }

}
