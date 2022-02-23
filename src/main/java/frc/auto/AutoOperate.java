package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.robot.Constants;
import frc.subsystems.Drive;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;
    private Drive drive;

    /**
     * Get the instance of the AutoOperator, if none create a new instance
     * 
     * @return instance of the AutoOperator
     */
    public static AutoOperate getInstance() {
        if (instance == null) {
            instance = new AutoOperate();
        }
        return instance;
    }

    private AutoOperate() {
        this.drive = Drive.getInstance();
    }

    @Override
    public void firstCycle(){
        this.drive.firstCycle();
        startTime = System.currentTimeMillis();
        try {
            data = CSVReader.convertToArrayList("test" + Constants.VERSION);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void calculate() {
        driveMode();
        this.drive.calculate();
    }

    /**
     * Shooter mode for autonomous
     */
    private void driveMode() {
        currentTime = System.currentTimeMillis() - startTime;

        try {
            if(currentTime < data.get(0).get(0).longValue()) {
                this.drive.setDriveLeft(data.get(0).get(1));
                this.drive.setDriveRight(data.get(0).get(2));
            } else {
                data.remove(0);
            }
            if(data.size() <= 0) {
                data.clear();
                this.drive.resetPosition();
                this.disable();
                return;
            }
        } catch (Exception e) {
            System.err.println("Autonomous Drive Mode Failed!");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void disable() {
        this.drive.calculate();
        this.drive.disable();
    }
    
}
