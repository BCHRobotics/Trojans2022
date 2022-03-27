package frc.auto;

import java.util.ArrayList;
import java.util.List;

import frc.io.subsystems.ArmIO;
import frc.io.subsystems.DriveIO;
import frc.io.subsystems.IntakeIO;
import frc.io.subsystems.ShooterIO;
import frc.robot.Constants;
import frc.util.csv.CSVWriter;

public class AutoBuilder {
    private static AutoBuilder instance;
    private static CSVWriter writer;
    private static List<List<Double>> data = new ArrayList<>();

    private static long startTime;
    private static long currentTime;
    private static long timer;

    private static DriveIO driveIO;
    private static IntakeIO intakeIO;
    private static ShooterIO shooterIO;
    private static ArmIO armIO;

    public static AutoBuilder getInstance() {
        if (instance == null) {
            instance = new AutoBuilder();
        }
        return instance;
    }

    private AutoBuilder(){
       writer = new CSVWriter(Constants.ROOT_DIRECTORY);
       driveIO = DriveIO.getInstance();
       intakeIO = IntakeIO.getInstance();
       shooterIO = ShooterIO.getInstance();
       armIO = ArmIO.getInstance();
    }

    public void setStartRecording() {
        startTime = System.currentTimeMillis();
    }
    
    public void recordData() {
        try {
            currentTime = System.currentTimeMillis();
            timer = currentTime - startTime;

            List<Double> rows = new ArrayList<Double>();
            driveIO.updateInputs();
            rows.add((double)timer);
            rows.add((double)driveIO.getDriveL1Encoder().getPosition());
            rows.add((double)driveIO.getDriveR1Encoder().getPosition());
            rows.add(intakeIO.getIntakeState() == true ? (double)1.0 : (double)0.0);
            rows.add((double)intakeIO.getIntakePercent());
            rows.add((double)intakeIO.getStagerPercent());
            rows.add((double)intakeIO.getFeederPercent());
            rows.add(intakeIO.getFeederArmState() == true ? (double)1.0 : (double)0.0);
            rows.add((double)armIO.getLeftArmEncoder().getPosition());
            rows.add((double)shooterIO.getInputWheelSpeed());
            data.add(rows);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    public void convertData() {
        try {
            writer.setFileName(Constants.TEACH_MODE_FILE_NAME);
            writer.deleteCopy();
            writer.setHeader("time,leftDrive,rightDrive,intakeArms,intakeRollers,stagerRollers,feederRollers,feederArm,armPosition,shooterWheels");
            writer.importData(data);
            writer.output();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}
