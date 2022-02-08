package frc.io.subsystems;

public class IO implements IIO{

    private static IO instance;

    private DriveIO driveIO;
    private ShooterIO shooterIO;

    public static IO getInstance() {
        if(instance == null) instance = new IO();
        return instance;
    }

    private IO() {
        this.driveIO = DriveIO.getInstance();
        this.shooterIO = ShooterIO.getInstance();
    }

    @Override
    public void updateInputs() {
        this.driveIO.updateInputs();
        this.shooterIO.updateInputs();
    }

    @Override
    public void resetInputs() {
        this.driveIO.resetInputs();
        this.shooterIO.resetInputs();
    }

    @Override
    public void stopAllOutputs() {
        this.driveIO.stopAllOutputs();
        this.shooterIO.stopAllOutputs();
    }

}
