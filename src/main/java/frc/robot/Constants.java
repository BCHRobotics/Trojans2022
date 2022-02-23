package frc.robot;

import frc.util.pid.PIDConstants;
import frc.util.pid.SparkMaxConstants;

public class Constants {
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;

    // CAN ID(s) for Drivetrain
    public static final int DRIVE_LEFT1_ID = 11;//10
    public static final int DRIVE_RIGHT1_ID = 15;//11
    public static final int DRIVE_LEFT2_ID = 12;//12
    public static final int DRIVE_RIGHT2_ID = 16;//13

    // CAN ID(s) for Intake / Stager
    public static final int INTKAE_ROLLER_ID = 20; //20
    public static final int STAGER_ROLLER_ID = 21; //21
    public static final int FEEDER_ROLLER_ID = 30; //22

    // CAN ID(s) for Shooter
    public static final int LEFT_SHOOTER_WHEEL_ID = 22;//30
    public static final int RIGHT_SHOOTER_WHEEL_ID = 57;//31

    // CAN ID(s) for Arm
    public static final int LEFT_ARM_ID = 40;
    public static final int RIGHT_ARM_ID = 41;

    // CAN ID(s) for Winch
    public static final int LEFT_WINCH_ID = 50;
    public static final int RIGHT_WINCH_ID = 51;

    // CSV Test version
    public static final int VERSION = 11;
    public static final String TEACH_MODE_FILE_NAME = "record";

    // Subsytems toggle logic
    public static final boolean SHOOTER_ENABLED = true;
    public static final boolean DRIVE_ENABLED = true;
    public static final boolean WINCH_ENABLED = true;
    public static final boolean ARM_ENABLED = true;
    public static final boolean INTAKE_ENABLED = true;
    public static final boolean MINI_BOT = false;

    // Shooter PID Constants
    public static final SparkMaxConstants SHOOTER_WHEEL_CONSTANTS = new SparkMaxConstants(6e-5, 0, 0, 0, 0.000015, -1, 1, 0, 0, 6000, 3000, 0);
    
    // Lift PID Constants
    public static final SparkMaxConstants ARM_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    
    // Winch PID Constants
    public static final SparkMaxConstants WINCH_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);

    // Drive PID Constants
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 2500, 0);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 2500, 0);
    
    //Auto directory
    public static final String ROOT_DIRECTORY = "csv/";

    // Limlight constants
    public static final double LIMELIGHT_HEIGHT = 0.25; // Height in meters
    public static final double LIMELIGHT_ANGLE = 45; // Angle in degrees
    public static final double TARGET_HEIGHT = 2.64; // Height in meters
    public static final double SHOOTER_HEIGHT = 0.50; // Height in meteres

    // Set shooter wheel speed constant
    public static final double LIFT_ARM_GEAR_REDUCTION = 525;
    public static final double CLIMBER_WINCH_ROTATIONS = 24;

    // EXPERIMENTAL PID TUNING
    public static final PIDConstants LIMELIGHT_ROTATE = new PIDConstants(0.125, 0.001, 1, 4);
}