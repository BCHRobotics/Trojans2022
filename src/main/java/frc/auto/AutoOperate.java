package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.robot.Constants;
import frc.subsystems.Shooter;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;

    private Shooter shooter;

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
        this.shooter = Shooter.getInstance();
    }

    @Override
    public void firstCycle(){
        this.shooter.firstCycle();
        startTime = System.currentTimeMillis();
        try {
            data = CSVReader.convertToArrayList("test");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void calculate() {
        shootMode();
        this.shooter.calculate();
    }

    /**
     * Shooter mode for autonomous
     */
    private void shootMode() {
        currentTime = System.currentTimeMillis() - startTime;

        try {
            if(currentTime >= data.get(0).get(0).longValue()) {
                this.shooter.setShooterTurretPosition(data.get(0).get(1));
                System.out.println(data.get(0).get(1));
                data.remove(0);
            }
        } catch (Exception e) {
            this.shooter.disable();
            return;
        }
    }

    @Override
    public void disable() {
        this.shooter.calculate();
        this.shooter.disable();
    }
    
}
