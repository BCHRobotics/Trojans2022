package frc.io;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.util.PIDConstants;

public class Dashboard {
    private static Dashboard instance;
    
    /**
     * Get the instance of the Dashboard, if none create a new instance
     * 
     * @return instance of the Dashboard
     */
    public static Dashboard getInstance() {
        if (instance == null) {
            instance = new Dashboard();
        }
        return instance;
    }

    private Dashboard() {

    }

    /**
     * Calls all the update methods to update the values on the SmartDashboard
     */
    public void updateAll() {
        updateSensorDisplay();
    }

    /**
     * Update sensor data to the SmartDashboard
     */
    public void updateSensorDisplay() {

    }

    /**
     * Get the PID constants modified by the SmartDashboard
     * 
     * @param name Name of the control system using these constants
     * @param constants Current/Default constants to return if none are found on dashboard
     * @return PIDConstants from the SmartDashboard
     */
	public PIDConstants getPIDConstants(String name, PIDConstants constants) {
		double p = SmartDashboard.getNumber(name + "-P", constants.p);
		double i = SmartDashboard.getNumber(name + "-I", constants.i);
		double d = SmartDashboard.getNumber(name + "-D", constants.d);
		double ff = SmartDashboard.getNumber(name + "-FF", constants.ff);
		double eps = SmartDashboard.getNumber(name + "-EPS", constants.eps);
		return new PIDConstants(p, i, d, ff, eps);
	}

	/**
     * Put the given PIDConstants values on the SmartDashboard
     * 
     * @param name Name of the control system using these constants
     * @param constants Constants that are to be displayed on the SmartDashboard
     */
	public void putPIDConstants(String name, PIDConstants constants) {
        SmartDashboard.putNumber(name + "-P", constants.p);
        SmartDashboard.putNumber(name + "-I", constants.i);
		SmartDashboard.putNumber(name + "-D", constants.d);
		SmartDashboard.putNumber(name + "-FF", constants.ff);
		SmartDashboard.putNumber(name + "-EPS", constants.eps);
	}

    // Get the PID Turn
	public double getPathTurnP() {
		return SmartDashboard.getNumber("Path Turn P", Constants.PATH_TURN_P);
	}
}
