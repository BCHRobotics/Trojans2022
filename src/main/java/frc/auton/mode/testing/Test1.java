package frc.auton.mode.testing;

import frc.auton.drive.DriveTurnToAngle;
import frc.auton.drive.DriveWait;
import frc.auton.mode.AutonBuilder;
import frc.auton.mode.AutonMode;

public class Test1 implements AutonMode{
    
    @Override
    public void addToMode(AutonBuilder ab) {
        //ab.addCommand(new DriveToPoint(30, 0, 45, 5, 11, 0.5, 15000));
        //ab.addCommand(new DriveToPoint(30, 0, 45, 0, 8, 2, 15000));
        ab.addCommand(new DriveTurnToAngle(90, 11, 0.5, 3000));
        ab.addCommand(new DriveWait());
    }

}
