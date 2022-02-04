package frc.io.subsystems;

public class IO implements IIO{

    private static IO instance;

    private DriveIO driveIO;

    public static IO getInstance() {
        if(instance == null) instance = new IO();
        return instance;
    }

    private IO() {
        this.driveIO = DriveIO.getInstance();
    }

    @Override
    public void updateInputs() {
        this.driveIO.updateInputs();
    }

    @Override
    public void resetInputs() {
        this.driveIO.resetInputs();
    }

    @Override
    public void stopAllOutputs() {
        this.driveIO.stopAllOutputs();
    }

}
