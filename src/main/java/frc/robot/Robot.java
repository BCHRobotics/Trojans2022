// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.auton.AutonControl;
import frc.imaging.Limelight;
import frc.io.Dashboard;
import frc.io.Input.SensorInput;
import frc.io.Output.RobotOutput;
import frc.subsystems.Climber;
import frc.subsystems.Drive;
import frc.subsystems.Climber.ClimberMode;
import frc.teleop.TeleopControl;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private RobotOutput robotOutput;
    private SensorInput sensorInput;
    private TeleopControl teleopControl;
    private Dashboard dashboard;
    private AutonControl autonControl;

    private Drive drive;

    private boolean pushToDashboard = true;
    public static boolean teleopInitialized = false;

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {

        if (this.pushToDashboard) Constants.pushValues();

        this.robotOutput = RobotOutput.getInstance();
        this.sensorInput = SensorInput.getInstance();
        this.teleopControl = TeleopControl.getInstance();
        this.dashboard = Dashboard.getInstance();
        this.autonControl = AutonControl.getInstance();

        this.drive = Drive.getInstance();

        this.sensorInput.reset();

        Limelight.getInstance().setLedMode(1);
        SmartDashboard.putNumber("Test", 0);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and
     * test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different
     * autonomous modes using the dashboard. The sendable chooser code works with
     * the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
     * chooser code and
     * uncomment the getString line to get the auto name from the text box below the
     * Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure
     * below with additional strings. If using the SendableChooser make sure to add
     * them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        this.robotOutput.drive.setDriveRampRate(0.0);

        this.autonControl.initialize();
        this.autonControl.setRunning(true);
        this.autonControl.setOverrideAuto(false);

        this.drive.firstCycle();
        this.sensorInput.reset();
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        this.sensorInput.update();
        this.dashboard.updateAll();

        this.autonControl.runCycle();
    }

    /** This function is called once when teleop is enabled. */
    @Override
    public void teleopInit() {
        if (!this.autonControl.isRunning()) {
            this.robotOutput.drive.setDriveRampRate(0.15);
            this.teleopControl.initialize();
            Robot.teleopInitialized = true;
        }
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {

        System.out.println("Started Teleop Periodic!");

        if (this.autonControl.isRunning()) {
            this.autonomousPeriodic();
        } else {
            if (!Robot.teleopInitialized) {
                this.teleopInit();
            }
            System.out.println("Teleop is running!");
            this.sensorInput.update();
            this.teleopControl.runCycle();
            this.dashboard.updateAll();
        }
    }

    /** This function is called once when the robot is disabled. */
    @Override
    public void disabledInit() {
        this.robotOutput.stopAll();
        this.teleopControl.disable();
    }

    /** This function is called periodically when disabled. */
    @Override
    public void disabledPeriodic() {
        this.sensorInput.update();
        this.dashboard.updateAll();
        this.autonControl.updateModes();
    }

    /** This function is called once when test mode is enabled. */
    @Override
    public void testInit() {
        this.sensorInput.reset();
        this.drive.firstCycle();

        if (this.pushToDashboard) Constants.pushValues();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
        this.sensorInput.update();
        this.dashboard.updateAll();

        /*Climber climber = Climber.getInstance();

        climber.setCurrentMode(ClimberMode.INDEPENDENT);

        climber.setLeftExtendOutput(0.3);
        climber.setRightExtendOutput(0.3);
        climber.setLeftRotateOutput(0.3);
        climber.setRightRotateOutput(0.3);

        climber.calculate();*/

        //double test = sensorInput.climber.getArmRotateLeftEncoder();
        //SmartDashboard.putNumber("Test", test);

        Drive drive = Drive.getInstance();
        
        drive.setOutput(0.3, 0);
        drive.calculate();
    }
}
