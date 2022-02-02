package frc.io.Input;

import com.revrobotics.RelativeEncoder;

import frc.io.Output.RobotOutput;
import frc.robot.Constants;

public class ClimberInput implements ISensorInput {
    private static ClimberInput instance;

    private boolean enabled = Constants.CLIMBER_ENABLED;

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
        if (!enabled) return;

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
        if (!enabled) return;

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
        if (!enabled) return;
    }

    /**
     * Get the position of the Right Arm Extend Encoder
     * @return position in inches
     */
    public double getArmExtendRightEncoder() {
        if (!enabled) return 0;
        return this.armExtendRightEncoder.getPosition();
    }

    /**
     * Get the position of the Left Arm Extend Encoder
     * @return position in inches
     */
    public double getArmExtendLeftEncoder() {
        if (!enabled) return 0;
        return this.armExtendLeftEncoder.getPosition();
    }

    /**
     * Get the position of the Right Arm Rotate Encoder
     * @return position in degrees
     */
    public double getArmRotateRightEncoder() {
        if (!enabled) return 0;
        return this.armRotateRightEncoder.getPosition();
    }

    /**
     * Get the position of the Left Arm Rotate Encoder
     * @return position in degrees
     */
    public double getArmRotateLeftEncoder() {
        if (!enabled) return 0;
        return this.armRotateLeftEncoder.getPosition();
    }

}
