package frc.robot;

import frc.io.Dashboard;
import frc.util.PIDConstants;

public class Constants {

    public static final boolean DRIVE_ENABLED = false;
    public static final boolean CLIMBER_ENABLED = true;
    
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WhHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;

    //#region PID

    private static PIDConstants driveStraightPID = new PIDConstants(0.5, 0, 4, 0.05); // Updated 2021-03-25
	private static PIDConstants driveTurnPID = new PIDConstants(0.15, 0.01, 0.65, 1);
	private static PIDConstants driveVelocityPID = new PIDConstants(0.0, 0, 0.0, 1.0 / 14.0, 0);

    private static Dashboard dashboard = Dashboard.getInstance();

    public static PIDConstants getDriveStraightPID() {
        if (USING_DASHBOARD) {
            return dashboard.getPIDConstants("DRIVE_PID", driveStraightPID);
        } else {
            return driveStraightPID;
        }
    }

    public static PIDConstants getDriveTurnPID() {
        if (USING_DASHBOARD) {
            return dashboard.getPIDConstants("TURN_PID", driveTurnPID);
        } else {
            return driveTurnPID;
        }
    }

    public static PIDConstants getDriveVelocityPID() {
        if (USING_DASHBOARD) {
            return dashboard.getPIDConstants("DRIVE_VELOCITY_PID", driveVelocityPID);
        } else {
            return driveVelocityPID;
        }
    }

    public static void pushValues() {
        dashboard.putPIDConstants("DRIVE_PID", driveStraightPID);
        dashboard.putPIDConstants("TURN_PID", driveTurnPID);
        dashboard.putPIDConstants("DRIVE_VELOCITY_PID", driveVelocityPID);
    }

    //#endregion PID
}
