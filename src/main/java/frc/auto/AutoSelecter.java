package frc.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class AutoSelecter {
    private static AutoSelecter instance;
    private static SendableChooser<String> autoChooser;

    public static AutoSelecter getInstance() {
        if (instance == null) {
            instance = new AutoSelecter();
        }
        return instance;
    }

    private AutoSelecter() {
        autoChooser = new SendableChooser<String>();
        autoChooser.setDefaultOption("AUTO_PATH_1", "AUTO_PATH_1");
        autoChooser.addOption("AUTO_PATH_2", "AUTO_PATH_2");
        autoChooser.addOption("AUTO_PATH_3", "AUTO_PATH_3");
        autoChooser.addOption("Live Record", Constants.TEACH_MODE_FILE_NAME);

        SmartDashboard.putData("Autonomous Route", autoChooser);
    }

    public String getFileName() {
        return autoChooser.getSelected();
    }
}
