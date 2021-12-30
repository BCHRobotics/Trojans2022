package frc.auton.drive;

import frc.auton.AutonCommand;
import frc.auton.RobotComponent;

public class DriveWait extends AutonCommand {

    public DriveWait() {
        super(RobotComponent.DRIVE);
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public boolean calculate() {
        return true;
    }

    @Override
    public void override() {

    }
    
}
