package frc.io.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This class initiates the Intake Input/Output
 * @author  Tim Rostorhuiev
 * @author  Ryan Moffatt 
 */
public class IntakeIO implements IIO{
    private static IntakeIO instance;

    private TalonSRX rollerSRX;
    private Solenoid unfoldIntake;

    /**
     * Making the IntakeIO a singleton instance
     * @return Singleton
     */
    public static IntakeIO getInstance() {
        if (instance == null) {
            instance = new IntakeIO();
        }
        return instance;
    }

    /**
     * Initializes all the variables
     */
    private IntakeIO() {
        this.rollerSRX = new TalonSRX(-1); // TODO: Set this to the right number 

        unfoldIntake = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

        this.rollerSRX.setInverted(false);
    }

    /**
     * Sets the roller motor
     * @param speed
     */
    public void setRollerSpeed(double speed) {
        this.rollerSRX.set(ControlMode.PercentOutput, speed);
    }

    /**
     * Set the pneumatics for the intake On/Off 
     * @param on
     */
    public void setUnfoldIntake(boolean on) {
        this.unfoldIntake.set(on);
    }
    
    /**
     * Update the state of the inputs
     */
    @Override
    public void updateInputs() {
        
    }

    /**
     * Reset the state of the inputs
     */
    @Override
    public void resetInputs() {
        this.rollerSRX.set(ControlMode.PercentOutput, 0);
        this.unfoldIntake.set(false);
    }

    /**
     * Stop all the outputs
     */
    @Override
    public void stopAllOutputs() {
        setRollerSpeed(0);
        setUnfoldIntake(false);
        
    }
    
}
