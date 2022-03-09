package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.robot.Constants;
import frc.subsystems.Climber;
import frc.subsystems.Drive;
import frc.subsystems.Intake;
import frc.subsystems.Shooter;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;
    private Drive drive;
    private Intake intake;
    private Shooter shooter;
    private Climber climber;

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
        this.shooter = Shooter.getInstance();
        this.climber = Climber.getInstance();
    }

    @Override
    public void firstCycle(){
        data.clear();
        this.drive.firstCycle();
        this.intake.firstCycle();
        this.shooter.firstCycle();
        this.climber.firstCycle();
        startTime = System.currentTimeMillis();
        try {
            System.out.println("Made it to firstCycle");
            data = CSVReader.convertToArrayList(AutoSelecter.getInstance().getFileName());//AutoSelecter.getInstance().getFileName()
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
        this.shooter.calculate();
        this.climber.calculate();
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
            if(currentTime < data.get(0).get(0).longValue() * 0.35) {
                this.drive.setDriveLeft(data.get(0).get(1));
                this.drive.setDriveRight(data.get(0).get(2));
                this.intake.setIntakeState(data.get(0).get(3) == 1.0 ? true : false);
                this.intake.setIntakeSpeed(data.get(0).get(4));
                this.intake.setStagerSpeed(data.get(0).get(5));
                this.intake.setFeederSpeed(data.get(0).get(6));
                this.intake.setFeederState(data.get(0).get(7) == 1.0 ? true : false);
                this.climber.setRobotArmPosition(data.get(0).get(8));
                this.shooter.setShooterWheelSpeed(data.get(0).get(9));
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
        this.shooter.disable();
        this.climber.disable();
    }
    
}
