package frc.robot;

import frc.util.pid.SparkMaxConstants;

public class Constants {
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;

    // CAN ID(s) for Drivetrain
    public static final int DRIVE_LEFT1_ID = 10;
    public static final int DRIVE_RIGHT1_ID = 11;

    // CSV Test version
    public static final int VERSION = 11;
    public static final String TEACH_MODE_FILE_NAME = "LIVE_RECORD";

    // Subsytems toggle logic
    public static final boolean DRIVE_ENABLED = true;
    public static final boolean MINI_BOT = false;

    // Drive PID Constants
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 3500, 0.1);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 3500, 0.1);
    
    //Auto directory
    public static final String ROOT_DIRECTORY = "csv/";
}