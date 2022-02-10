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

    public void setStartRecording() {
        startTime = System.currentTimeMillis();
    }
    
    public void recordData() {

        try {
            currentTime = System.currentTimeMillis();
            data.get(data.size()).add((double)currentTime - (double)startTime);
            data.get(data.size()).add(shooterIO.getTurretEncoder().getPosition());
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

    }

    public void convertData() {
        try {
            writer.setFileName("test6");
            writer.setHeader("time,motor");
            writer.importData(data);
            writer.output();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }
}
