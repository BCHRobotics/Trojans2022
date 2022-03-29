package frc.commands.intake;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import frc.commands.Command;
import frc.robot.Constants;
import frc.subsystems.Intake;

public class Stage extends Command{
    private static Stage instance;

    private boolean isFinished;

    private Intake intake;
    
    private ColorSensorV3 colorSensor = new ColorSensorV3(Constants.I2C_PORT);
    private ColorMatch colorMatch = new ColorMatch();
    private ColorMatchResult matchResult;
    private Color gameColor;
    private Color detectedColor;

    public static Stage getInstance() {
        if (instance == null) {
            instance = new Stage();
        }
        return instance;
    }

    private Stage() {
        this.intake = Intake.getInstance();
    }

    @Override
    public void initialize() {
        this.intake.firstCycle();
        this.isFinished = false;

        this.colorMatch.addColorMatch(Constants.COLOR_RED);
        this.colorMatch.addColorMatch(Constants.COLOR_BLUE);
    }

    @Override
    public void calculate() {
        this.intake.setStagerSpeed(1);
    }

    public void reverse() {
        this.intake.setStagerSpeed(-1);
    }

    @Override
    public void execute() {
        this.intake.run();
    }

    @Override
    public void end() {
        this.intake.setStagerSpeed(0);
        this.isFinished = true;
    }

    @Override
    public boolean isFinished() {
        return this.isFinished;
    }

    @Override
    public void disable() {
        this.intake.disable();
    }

    public boolean colorMatches() {
        this.detectedColor = this.colorSensor.getColor();
        this.matchResult = this.colorMatch.matchClosestColor(this.detectedColor);

        if (DriverStation.getAlliance() == Alliance.Blue) {
            this.gameColor = Constants.COLOR_BLUE;
        } else if (DriverStation.getAlliance() == Alliance.Red) {
            this.gameColor = Constants.COLOR_RED;
        } else {
            this.gameColor = null;
        }

        if (this.matchResult.color == this.gameColor) {
            return true;
        } else return false;
    }
    
    public boolean cargoPresent() {
        if (this.colorSensor.getProximity() > Constants.PROXIMITY) {
            return true;
        } else return false;
    }
}
