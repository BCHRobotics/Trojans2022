package frc.auto;

import java.util.ArrayList;
import java.util.List;

import frc.io.subsystems.ShooterIO;
import frc.robot.Constants;
import frc.util.csv.CSVWriter;

public class AutoBuilder {
    private static AutoBuilder instance;
    private static CSVWriter writer;
    private static List<List<Double>> data = new ArrayList<>();
    private static List<Double> row = new ArrayList<>();

    private static long startTime;
    private static long currentTime;

    private static ShooterIO shooterIO;

    public static AutoBuilder getInstance() {
        if (instance == null) {
            instance = new AutoBuilder();
        }
        return instance;
    }

    private AutoBuilder(){
       writer = new CSVWriter(Constants.rootDirectory);
       shooterIO = ShooterIO.getInstance();
       shooterIO.stopAllOutputs();
    }

    public void convertData() {
        try {
            writer.setFileName("test3");
            writer.setHeader("time,motor");
            writer.importData(data);
            writer.output();
        } catch (Exception e) {
            return;
        }
    }

    public void setStartRecording() {
        startTime = System.currentTimeMillis();
    }
    
    public void recordData() {

        currentTime = System.currentTimeMillis();
        
        row.add((double)currentTime - (double)startTime);
        row.add(shooterIO.getTurretEncoder().getPosition());

        data.add(row);

    }
}
