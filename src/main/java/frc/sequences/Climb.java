package frc.sequences;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.commands.climb.*;

public class Climb extends Sequence{
    private static Climb instance;

    private boolean isFinished;

    private boolean verified;

    private int counter;

    private Lift winchCommand;
    private Swing armCommand;

    public static Climb getInstance() {
        if (instance == null) {
            instance = new Climb();
        }
        return instance;
    }

    private Climb() {
        this.winchCommand = Lift.getInstance();
        this.armCommand = Swing.getInstance();
    }

    @Override
    public void initialize() {
        this.winchCommand.initialize();
        this.armCommand.initialize();
        this.isFinished = false;
        this.counter = 0;
    }

    @Override
    public void calculate() {

        switch (this.counter) {
            case 1:
                this.armCommand.setArmPosition(-1);
                if (this.armCommand.armReachedPosition()) this.verified = true;
                else this.verified = false;
                break;
            case 2:
                this.winchCommand.setArmHeight(0);
                if (this.winchCommand.armReachedHeight()) this.verified = true;
                else this.verified = false;
                break;
            case 3:
                this.armCommand.setArmPosition(0);
                if (this.armCommand.armReachedPosition()) this.verified = true;
                else this.verified = false;
                break;
            case 4:
                this.winchCommand.setArmHeight(0.5);
                if (this.winchCommand.armReachedHeight()) this.verified = true;
                else this.verified = false;
                break;
            case 5:
                this.winchCommand.setArmHeight(0.5);
                if (this.winchCommand.armReachedHeight()) this.verified = true;
                else this.verified = false;
                break;
            case 6:
                this.armCommand.setArmPosition(1);
                if (this.armCommand.armReachedPosition()) this.verified = true;
                else this.verified = false;
                break;
            case 7:
                this.winchCommand.setArmHeight(1);
                if (this.winchCommand.armReachedHeight()) this.verified = true;
                else this.verified = false;
                break;
            case 8:
                this.armCommand.setArmPosition(0.7);
                if (this.armCommand.armReachedPosition()) this.verified = true;
                else this.verified = false;
                break;
            case 9:
                this.winchCommand.setArmHeight(0.5);
                this.armCommand.setArmPosition(0);
                if (this.armCommand.armReachedPosition() && this.winchCommand.armReachedHeight()) this.verified = true;
                else this.verified = false;
                break;
            case 10:
                this.counter = 0;
                this.verified = true;
                break;
        }

        this.winchCommand.calculate();
        this.armCommand.calculate();

        SmartDashboard.putNumber("Climb Step", this.counter);
    }

    public void advance() {
        if (this.verified) {
            this.counter++;
            this.verified = false;
        }
    }

    public void reverse() {
        if (this.verified) {
            this.counter--;
            this.verified = false;
        }
    }

    @Override
    public void execute() {
        this.winchCommand.execute();
        this.armCommand.execute();
    }

    @Override
    public void end() {
        this.isFinished = true;

        this.winchCommand.end();
        this.armCommand.end();
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.winchCommand.end();
        this.armCommand.end();
    }
    
}
