package frc.auto;

import java.util.ArrayList;
import java.util.List;

import frc.io.subsystems.DriveIO;
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

    public static AutoBuilder getInstance() {
        if (instance == null) {
            instance = new AutoBuilder();
        }
        return instance;
    }

    private AutoBuilder(){
       writer = new CSVWriter(Constants.ROOT_DIRECTORY);
       driveIO = DriveIO.getInstance();
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
            data.add(rows);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    public void convertData() {
        try {
            System.out.println("Made it to convertData()!");
            writer.setFileName("test" + Constants.VERSION);
            writer.setHeader("time,leftMotor,rightMotor");
            writer.importData(data);
            writer.output();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}
