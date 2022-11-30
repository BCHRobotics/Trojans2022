package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.subsystems.Drivetrain;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;
    private Drivetrain drive;
    private boolean disabled;
    // private Manual manualShoot;

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
        this.drive = Drivetrain.getInstance();
    }

    @Override
    public void firstCycle(){
        data.clear();
        this.drive.firstCycle();
        this.disabled = false;
        startTime = System.currentTimeMillis();
        try {
            data = CSVReader.convertToArrayList(AutoSelecter.getInstance().getFileName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void run() {
        if (!this.disabled) {
            driveMode();
            this.drive.setPositionMode(true);
            this.drive.brake(true);
            this.drive.run();
        } else {
            this.disable();
        }
    }

    /**
     * Shooter mode for autonomous
     */
    private void driveMode() {
        currentTime = System.currentTimeMillis() - startTime;

        try {
            if(data.size() <= 0) {
                this.drive.resetPosition();
                this.drive.setPositionMode(false);
                this.drive.brake(false);
                this.disable();
                data.clear();
                return;
            }
            if (currentTime < data.get(0).get(0).longValue() * 0.35) {
                if (data.get(0).get(4) == 0.0) this.drive.setDriveLeft(data.get(0).get(1));
                if (data.get(0).get(4) == 0.0) this.drive.setDriveRight(data.get(0).get(2));
            } else {
                data.remove(0);
            }
        } catch (Exception e) {
            System.err.println("Autonomous Drive Mode Failed!");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.disabled = true;
    }
    
}
