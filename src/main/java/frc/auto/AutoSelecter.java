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
        autoChooser.setDefaultOption("2_BALL_LOW", "AUTO_PATH_1");
        autoChooser.addOption("2_BALL_HIGH", "AUTO_PATH_2");
        autoChooser.addOption("3_BALL_HIGH", "AUTO_PATH_3");
        autoChooser.addOption("4_BALL_HIGH", "AUTO_PATH_4");
        autoChooser.addOption("LIVE_RECORD", Constants.TEACH_MODE_FILE_NAME);

        SmartDashboard.putData("Autonomous Route", autoChooser);
    }

    public String getFileName() {
        return autoChooser.getSelected();
    }
}
