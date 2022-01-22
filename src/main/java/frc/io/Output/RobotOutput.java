package frc.io.Output;

/**
 * This class is used to initiate all the Output Classes
 */
public class RobotOutput implements IRobotOutput {
    private static RobotOutput instance;

    public DriveOutput drive;
    public ClimberOutput climber;

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
        this.climber = new ClimberOutput();
    }

    @Override
    public void stopAll() {
        this.drive.stopAll();
    }

}
