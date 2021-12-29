package frc.io.Output;

/**
 * This class is used to initiate all the Output Classes
 */
public class RobotOutput {
    private static RobotOutput instance;

    public DriveOutput drive;

    /**
     * Get the instance of the RobotOutput, if none create a new instance
     * 
     * @return instance of the RobotOutput
     */
    public static RobotOutput getInstance() {
        if (instance == null) {
            instance = new RobotOutput();
        }
        return instance;
    }

    private RobotOutput() {
        this.drive = new DriveOutput();
    }


}
