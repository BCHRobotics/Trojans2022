package frc.io.Input;

public class SensorInput implements ISensorInput {
    private static SensorInput instance;

    public DriveInput drive; 
    public ClimberInput climber;

    /**
     * Get the instance of the SensorInput, if none create a new instance
     * 
     * @return instance of the SensorInput
     */
    public static SensorInput getInstance() {
        if (instance == null) {
            instance = new SensorInput();
        }
        return instance;
    }

    private SensorInput() {
        this.drive = DriveInput.getInstance();
        this.climber = ClimberInput.getInstance();
    }

    @Override
    public void reset() {
        this.drive.reset();
    }

    @Override
    public void update() {
        this.drive.update();
    }
}
