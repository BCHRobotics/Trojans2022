package frc.robot;

import frc.util.pid.SparkMaxConstants;

public class Constants {
    
    public static final boolean USING_DASHBOARD = true;
    public static final double CONTROLLER_DEADZONE = 0.1;
    public static final double WHEEL_DIAMETER = 6;
    public static final double MAX_OUTPUT = 1;
    public static final double PATH_TURN_P = 6;

    // CAN ID(s) for Drivetrain
    public static final int driveL1ID = 10;
    public static final int driveR1ID = 11;
    public static final int driveL2ID = 12;
    public static final int driveR2ID = 13;

    // CAN ID(s) for Intake / Stager
    public static final int intakeRollerID = 20;
    public static final int stagerRollerID = 21;
    public static final int feederRollerID = 22;

    // CAN ID(s) for Shooter
    public static final int leftShooterWheelID = 30;
    public static final int rightShooterWheelID = 31;

    // CAN ID(s) for Arm
    public static final int leftArmID = 40;
    public static final int rightArmID = 41;

    // CAN ID(s) for Winch
    public static final int leftWinchID = 50;
    public static final int rightWinchID = 51;

    // CSV Test version
    public static int version = 2;

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
    public static final SparkMaxConstants DRIVEL1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 1000, 0);
    public static final SparkMaxConstants DRIVER1_CONSTANTS = new SparkMaxConstants(1e-4, 0, 0, 0, 0.000156, -1, 1, 0, 0, 3000, 1000, 0);
    
    //Auto directory
    public static final String rootDirectory = "csv/";

}
