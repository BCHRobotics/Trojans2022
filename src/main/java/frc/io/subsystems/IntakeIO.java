package frc.io.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

// Setting variable rollerSRX snd unfoldIntake 
public class IntakeIO implements IIO{
    private static IntakeIO instance;

    private TalonSRX rollerSRX;
    private Solenoid unfoldIntake;

    // 
    public static IntakeIO getInstance() {
        if (instance == null) {
            instance = new IntakeIO();
        }
        return instance;
    }

    private IntakeIO() {
        this.rollerSRX = new TalonSRX(-1); // TODO: Set this to the right number 

        unfoldIntake = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

        this.rollerSRX.setInverted(false);
    }

     public void setRollerSpeed(double speed) {
        this.rollerSRX.set(ControlMode.PercentOutput, speed);
    }

    public void setUnfoldIntake(boolean on) {
        this.unfoldIntake.set(on);
    }
    
    @Override
    public void updateInputs() {
        
    }

    @Override
    public void resetInputs() {
        this.rollerSRX.set(ControlMode.PercentOutput, 0);
        this.unfoldIntake.set(false);
    }

    @Override
    public void stopAllOutputs() {
        setRollerSpeed(0);
        setUnfoldIntake(false);
        
    }
    

    
}
