package frc.robot;

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
    public static final int driveR1ID = 15;
    public static final int driveL2ID = 12;
    public static final int driveR2ID = 16;

    public static int version = 2;

    public static final boolean SHOOTER_ENABLED = true;
    public static final boolean DRIVE_ENABLED = true;
    public static final boolean MINI_BOT = false;

    // Shooter PID Constants
    public static final SparkMaxConstants TURRET_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    public static final SparkMaxConstants WHEEL_CONSTANTS = new SparkMaxConstants(6e-5, 0, 0, 0, 0.000015, -1, 1, 0, 0, 6000, 3000, 0);

    // Drive PID Constants
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 1000, 0);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 1000, 0);
    
    //Auto directory
    public static final String rootDirectory = "csv/";

}
