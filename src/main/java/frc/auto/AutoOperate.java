package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.robot.Constants;
import frc.subsystems.Drive;
import frc.subsystems.Intake;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;
    private AutoSelecter selecter;
    private Drive drive;
    private Intake intake;

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
        this.intake = Intake.getInstance();
    }

    @Override
    public void firstCycle(){
        data.clear();
        this.drive.firstCycle();
        this.intake.firstCycle();
        startTime = System.currentTimeMillis();
        try {
            System.out.println("Made it to firstCycle");
            data = CSVReader.convertToArrayList(AutoSelecter.getInstance().getFileName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void calculate() {
        driveMode();
        this.drive.calculate();
        this.intake.calculate();
    }

    /**
     * Shooter mode for autonomous
     */
    private void driveMode() {
        currentTime = System.currentTimeMillis() - startTime;

        try {
            if(data.size() <= 0) {
                this.drive.resetPosition();
                this.intake.resetPosition();
                this.disable();
                data.clear();
                return;
            }
            if(currentTime < data.get(0).get(0).longValue() * 0.25) {
                this.drive.setDriveLeft(data.get(0).get(1));
                this.drive.setDriveRight(data.get(0).get(2));
                this.intake.setIntakeState(data.get(0).get(3) == 1.0 ? true : false);
                this.intake.setIntakeSpeed(data.get(0).get(4));
                this.intake.setStagerSpeed(data.get(0).get(5));
            } else {
                data.remove(0);
            }
        } catch (Exception e) {
            System.err.println("Autonomous Drive Mode Failed!");
            //e.printStackTrace();
            return;
        }
    }

    @Override
    public void disable() {
        this.drive.disable();
        this.intake.disable();
    }
    
}
