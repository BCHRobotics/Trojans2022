package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import frc.util.pid.SparkMaxConstants;

public class Constants {
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;

    // CAN ID(s) for Drivetrain
    public static final int DRIVE_LEFT1_ID = 10;//11;//10
    public static final int DRIVE_RIGHT1_ID = 11;//15;//11
    public static final int DRIVE_LEFT2_ID = 12;//12;//12
    public static final int DRIVE_RIGHT2_ID = 13;//16;//13

    // CAN ID(s) for Intake / Stager
    public static final int INTKAE_ROLLER_ID = 20; //20
    public static final int STAGER_ROLLER_ID = 21; //21
    public static final int FEEDER_ROLLER_ID = 22; //22

    // CAN ID(s) for Shooter
    public static final int LEFT_SHOOTER_WHEEL_ID = 30;//30
    public static final int RIGHT_SHOOTER_WHEEL_ID = 31;//31

    // CAN ID(s) for Arm
    public static final int LEFT_ARM_ID = 40;//40
    public static final int RIGHT_ARM_ID = 41;//41

    // CAN ID(s) for Winch
    public static final int LEFT_WINCH_ID = 50;//50
    public static final int RIGHT_WINCH_ID = 51;//51

    // CSV Test version
    public static final int VERSION = 11;
    public static final String TEACH_MODE_FILE_NAME = "LIVE_RECORD";

    // Subsytems toggle logic
    public static final boolean SHOOTER_ENABLED = true;
    public static final boolean DRIVE_ENABLED = true;
    public static final boolean WINCH_ENABLED = true;
    public static final boolean ARM_ENABLED = true;
    public static final boolean INTAKE_ENABLED = true;
    public static final boolean MINI_BOT = false;

    // Shooter PID Constants
    public static final SparkMaxConstants LEFT_SHOOTER_WHEEL_CONSTANTS = new SparkMaxConstants(0.000545, 0, 0, 0, 0.000178, -1, 1, 0, 0, 6000, 3000, 0.1);
    public static final SparkMaxConstants RIGHT_SHOOTER_WHEEL_CONSTANTS = new SparkMaxConstants(0.000545, 0, 0, 0, 0.000183, -1, 1, 0, 0, 6000, 3000, 0.1);

    // Lift PID Constants
    public static final SparkMaxConstants ARM_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 2000, 0);
    
    // Winch PID Constants
    public static final SparkMaxConstants WINCH_CONSTANTS = new SparkMaxConstants(5e-5, 1e-6, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 2500, 0);

    // Drive PID Constants
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 3500, 0);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 6000, 3500, 0);
    
    //Auto directory
    public static final String ROOT_DIRECTORY = "csv/";

    // Limlight constants
    public static final double LIMELIGHT_HEIGHT = 1.0287; // Height in meters
    public static final double LIMELIGHT_ANGLE = 23; // Angle in degrees
    public static final double TARGET_HEIGHT = 2.64; // Height in meters
    public static final double SHOOTER_HEIGHT = 0.6096; // Height in meteres
    public static final double CHASIS_LEFT_CONVERSION = 11.2835522805; // Left encoder conversion factor (90 degrees / Left encoder revolutions)
    public static final double CHASIS_RIGHT_CONVERSION = -9.4263945489; // Right encoder conversion factor (90 degrees / Right encoder revolutions)

    // Set climber arm constants
    public static final double LIFT_ARM_GEAR_REDUCTION = 525;
    public static final double CLIMBER_WINCH_ROTATIONS = 120;

    // Pneumatic control module channels
    public static final int INTAKE_LOWERED = 0;
    public static final int INTAKE_RAISED = 1;

    public static final int FEEDER_RAISED = 6;
    public static final int FEEDER_LOWERED = 7;

    // Shooter angle
    public static final int ANGLE = 60; // Arm angle in degrees
    public static final double ANGLE_LIMIT = 40; // Arm angle in revolutions
    public static final int LIMIT_SWITCH_PORT = 9; // DIO port 9

    // Color sensor constants
    public static final I2C.Port I2C_PORT = I2C.Port.kOnboard;
    public static final Color COLOR_RED = new Color(0.561, 0.232, 0.114);
    public static final Color COLOR_BLUE = new Color(0.143, 0.427, 0.429);
    public static final int PROXIMITY = 500;
}