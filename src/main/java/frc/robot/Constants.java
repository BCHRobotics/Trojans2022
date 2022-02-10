package frc.robot;

import frc.io.Dashboard;
import frc.util.pid.PIDConstants;
import frc.util.pid.SparkMaxConstants;

public class Constants {
    
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WhHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;
    
    public static final int wheelID = 22;
    public static final int turretID = 21;

    public static final int driveL1ID = 11;
    public static final int driveL2ID = 12;
    public static final int driveR1ID = 15;
    public static final int driveR2ID = 16;

    // Shooter PID Constants
    public static final SparkMaxConstants TURRET_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    public static final SparkMaxConstants WHEEL_CONSTANTS = new SparkMaxConstants(6e-5, 0, 0, 0, 0.000015, -1, 1, 0, 0, 6000, 3000, 0);

    // Drive PID Constants
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    
    //Auto directory
    public static final String rootDirectory = "csv/";

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
