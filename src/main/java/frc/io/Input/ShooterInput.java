package frc.io.Input;

import com.revrobotics.RelativeEncoder;

import frc.io.Output.RobotOutput;
import frc.robot.Constants;

public class ShooterInput implements ISensorInput {
    private static ShooterInput instance;

    private boolean enabled = Constants.SHOOTER_ENABLED;

    private RobotOutput robotOutput;

    private RelativeEncoder wheelEncoder;
    private RelativeEncoder turretEncoder;

    public static ShooterInput getInstance() {
        if (instance == null) {
            instance = new ShooterInput();
        }
        return instance;
    }

    /**
     * Initiates the Shooter Input, gets the encoders from RobotOutput and assigns them
     */
    private ShooterInput() {
        if (!enabled) return;

        this.robotOutput = RobotOutput.getInstance();

        this.wheelEncoder = robotOutput.shooter.getWheelEncoder();
        this.turretEncoder = robotOutput.shooter.getTurretEncoder();
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void reset() {
        if (!enabled) return;

        this.wheelEncoder.setPosition(0);
        this.turretEncoder.setPosition(0);
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
    public double getWheelEncoder() {
        if (!enabled) return 0;
        return this.wheelEncoder.getVelocity();
    }

    /**
     * Get the position of the Left Arm Extend Encoder
     * @return position in inches
     */
    public double getTurretEncoder() {
        if (!enabled) return 0;
        return this.turretEncoder.getPosition();
    }
}
