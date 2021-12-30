package frc.auton.util;

import frc.auton.AutonCommand;
import frc.auton.RobotComponent;
import frc.subsystems.Drive;

public class RunFirstCycle extends AutonCommand {

    private Drive drive;

    public RunFirstCycle() {
        super(RobotComponent.UTIL);
        this.drive = Drive.getInstance();
    }

    @Override
    public void firstCycle() {
        this.drive.firstCycle();
    }

    @Override
    public boolean calculate() {
        return true;
    }

    @Override
    public void override() {

    }

}
