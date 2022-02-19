package frc.io.subsystems;

public class IO implements IIO{

    private static IO instance;

    private DriveIO driveIO;
    private IntakeIO intakeIO;
    private ShooterIO shooterIO;
    private ArmIO armIO;
    private WinchIO winchIO;

    public static IO getInstance() {
        if(instance == null) instance = new IO();
        return instance;
    }

    private IO() {
        this.driveIO = DriveIO.getInstance();
        this.intakeIO = IntakeIO.getInstance();
        this.shooterIO = ShooterIO.getInstance();
        this.armIO = ArmIO.getInstance();
        this.winchIO = WinchIO.getInstance();
    }

    @Override
    public void updateInputs() {
        this.driveIO.updateInputs();
        this.intakeIO.updateInputs();
        this.shooterIO.updateInputs();
        this.armIO.updateInputs();
        this.winchIO.updateInputs();
    }

    @Override
    public void resetInputs() {
        this.driveIO.resetInputs();
        this.intakeIO.resetInputs();
        this.shooterIO.resetInputs();
        this.armIO.resetInputs();
        this.winchIO.resetInputs();
    }

    @Override
    public void stopAllOutputs() {
        this.driveIO.stopAllOutputs();
        this.intakeIO.stopAllOutputs();
        this.shooterIO.stopAllOutputs();
        this.armIO.stopAllOutputs();
        this.winchIO.stopAllOutputs();
    }

}
