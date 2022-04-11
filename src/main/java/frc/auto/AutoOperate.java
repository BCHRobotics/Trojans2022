package frc.auto;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import frc.commands.intake.Collect;
import frc.commands.intake.Stage;
import frc.commands.shoot.Fire;
import frc.commands.shoot.Manual;
import frc.commands.shoot.Poly;
import frc.sequences.Shoot;
import frc.subsystems.Drivetrain;
import frc.subsystems.Intake;
import frc.subsystems.Shooter;
import frc.util.csv.CSVReader;

public class AutoOperate extends AutoComponent {
    private static AutoOperate instance;
    private static List<List<Double>> data = new ArrayList<>();
    private static long startTime;
    private static long currentTime;
    private Drivetrain drive;
    private Collect intake;
    private boolean disabled;
    // private Stage stager;
    // private Poly feeder;
    // private Fire launch;
    // private Manual manualShoot;
    private Shoot shootSequence;

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
        this.intake = Collect.getInstance();
        // this.stager = Stage.getInstance();
        // this.feeder = Poly.getInstance();
        // this.launch = Fire.getInstance();
        // this.manualShoot = Manual.getInstance();
        this.shootSequence = Shoot.getInstance();
    }

    @Override
    public void firstCycle(){
        data.clear();
        this.drive.firstCycle();
        this.intake.initialize();
        // this.stager.initialize();
        // this.feeder.initialize();
        // this.launch.initialize();
        // this.manualShoot.initialize();
        this.shootSequence.initialize();
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
            this.intake.execute();
            // this.stager.execute();
            // this.feeder.execute();
            // this.launch.execute();
            // this.manualShoot.execute();
            this.shootSequence.execute();
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
                data.clear();
                this.disable();
                return;
            }
            if (currentTime < data.get(0).get(0).longValue() * 0.35) {
                this.drive.setDriveLeft(data.get(0).get(1));
                this.drive.setDriveRight(data.get(0).get(2));
                if (data.get(0).get(3) == 1.0) this.intake.calculate(); else this.intake.end();
                // if (data.get(0).get(5) == 1.0) this.stager.calculate(); else this.stager.end();
                // if (data.get(0).get(6) == 1.0) this.feeder.calculate(); else this.feeder.end();
                // if (data.get(0).get(7) == 1.0) this.launch.calculate(); else this.launch.end();
                // this.manualShoot.setShooterSpeed(data.get(0).get(9));
                if (data.get(0).get(10) == 1.0) this.shootSequence.calculate(); else this.shootSequence.end();
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
        this.intake.disable();
        // this.stager.disable();
        // this.feeder.disable();
        // this.launch.disable();
        // this.manualShoot.disable();
        this.shootSequence.disable();
        this.disabled = true;
    }
    
}
