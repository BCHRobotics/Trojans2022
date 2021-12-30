package frc.auton;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.auton.mode.AutonBuilder;
import frc.auton.mode.AutonMode;
import frc.auton.mode.testing.Test1;
import frc.io.DriverInput;
import frc.io.Output.RobotOutput;
import frc.robot.Robot;
import frc.teleop.TeleopControl;

public class AutonControl {
    
    private static AutonControl instance;
    private TeleopControl teleopControl;
    private DriverInput driverInput;

    private boolean overrideAuto = false;
    private boolean hasTapped = false;
    private boolean released = false;

    private long doubleTapLength = 500;
    private long timeOfFirstTap;
    private long timeOfTap;

    private String autoSelectError = "NO ERROR";

    public static final int NUM_ARRAY_MODE_STEPS = 1;

    private int autonDelay;
    private long autonStartTime;

    private boolean running = false;

    private int curAutonStepToSet = 0;
    private int[] autonSubmodeSelections = new int[NUM_ARRAY_MODE_STEPS];
    private ArrayList<ArrayList<AutonMode>> autonSteps = new ArrayList<>();

    private int currIndex;
    private AutonCommand[] commands;

    public static AutonControl getInstance() {
        if (instance == null) {
            instance = new AutonControl();
        }
        return instance;
    }

    private AutonControl() {
        this.teleopControl = TeleopControl.getInstance();
        this.driverInput = DriverInput.getInstance();
        this.autonDelay = 0;
        this.currIndex = 0;

        for (int i = 0; i < NUM_ARRAY_MODE_STEPS; i++) {
            this.autonSteps.add(new ArrayList<AutonMode>());
            this.autonSubmodeSelections[i] = 0; // default to default auto modes
        }

        ArrayList<AutonMode> autos = this.autonSteps.get(0);

        autos.add(new Test1());
    }

    public void initialize() {
        System.out.println("START AUTO");

        this.currIndex = 0;
        this.running = true;

        // initialize auton in runCycle
        AutonBuilder ab = new AutonBuilder();

        // add auton commands from all the different steps
        for (int i = 0; i < this.autonSteps.size(); i++) {
            this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).addToMode(ab);
        }

        // get the full auton mode
        this.commands = ab.getAutonList();

        this.autonStartTime = System.currentTimeMillis();

        // clear out each components "run seat"
        AutonCommand.reset();

       
    }

    public void runCycle() {
        // haven't initialized list yet
        long timeElapsed = System.currentTimeMillis() - this.autonStartTime;

        if (!this.hasTapped) {
            if (this.driverInput.getAutoOverride()) {
                this.hasTapped = true;
                this.released = false;
                this.timeOfFirstTap = System.currentTimeMillis();

            }
        } else {
            if (!this.driverInput.getAutoOverride()) {
                this.released = true;

            }
        }

        if (this.released) {
            if (this.driverInput.getAutoOverride()) {
                this.timeOfTap = System.currentTimeMillis() - this.timeOfFirstTap;
                if (this.timeOfTap < this.doubleTapLength) {
                    this.overrideAuto = true;
                    this.teleopControl.initialize();
                    Robot.teleopInitialized = true;
                } else {
                    this.hasTapped = true;
                    this.timeOfFirstTap = System.currentTimeMillis();
                    this.released = false;
                }
            }
        }

        if (this.overrideAuto) {
            this.running = false;
            this.teleopControl.runCycle();
        } else {
            if (timeElapsed > this.getAutonDelayLength() && this.running) {
                // System.out.println("Current index " + this.currIndex);

                // start waiting commands
                while (this.currIndex < this.commands.length && this.commands[this.currIndex].checkAndRun()) {
                    this.currIndex++;

                }
                // calculate call for all running commands
                AutonCommand.execute();
            } else {
                RobotOutput.getInstance().stopAll();
            }
        }

    }

    public void stop() {
        this.running = false;
    }

    public long getAutonDelayLength() {
        return this.autonDelay * 500;
    }

    public void updateModes() {
        DriverInput driverIn = DriverInput.getInstance();

        if (driverIn.getAutonStepIncrease()) {
            this.curAutonStepToSet++;
            this.curAutonStepToSet = Math.min(this.curAutonStepToSet, this.autonSteps.size() - 1);
        }

        if (driverIn.getAutonStepDecrease()) {
            this.curAutonStepToSet--;
            this.curAutonStepToSet = Math.max(this.curAutonStepToSet, 0);
        }

        boolean updatingAutoMode = false;

        try {

            int val = 0;
            if (driverIn.getAutonModeIncrease()) {
                val = 1;
            } else if (driverIn.getAutonModeIncreaseBy10()) {
                val = 10;
            } else if (driverIn.getAutonModeDecrease()) {
                val = -1;
            } else if (driverIn.getAutonModeDecreaseBy10()) {
                val = -10;
            }

            if (val != 0) {
                updatingAutoMode = true;

                // figure out which auton mode is being selected
                int autonMode = this.autonSubmodeSelections[this.curAutonStepToSet] + val;

                // make sure we didn't go off the end of the list
                autonMode = Math.min(autonMode, this.autonSteps.get(this.curAutonStepToSet).size() - 1);
                if (autonMode < 0) {
                    autonMode = 0;
                }

                this.autonSubmodeSelections[this.curAutonStepToSet] = autonMode;

                /*
                 * if(val < 0) { this.autonMode = 0; } else { this.autonMode = 1; }
                 */
            } else if (driverIn.getAutonSetDelayButton()) {
                this.autonDelay = (int) ((driverIn.getAutonDelayStick() + 1) * 5.0);
                if (this.autonDelay < 0) {
                    this.autonDelay = 0;
                }
            }

        } catch (Exception e) {
            // this.autonMode = 0;
            

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            this.autoSelectError = sw.toString();

        }

        // display steps of auto
        for (int i = 0; i < autonSteps.size(); i++) {
            // name of the current auton mode
            String name = this.autonSteps.get(i).get(this.autonSubmodeSelections[i]).getClass().getName();

            // make sure there is a '.'
            if (name.lastIndexOf('.') >= 0) {
                // get just the last bit of the name
                name = name.substring(name.lastIndexOf('.'));
            }

            String outputString = "" + autonSubmodeSelections[i] + name + "";

            SmartDashboard.putString("12_Auton Step " + (i + 1) + ": ", outputString);

            if (updatingAutoMode) {
                // System.out.print(this.autonSubmodeSelections[i] + "-");
                System.out.println("Step " + (i + 1) + ": " + outputString);
            }

            // System.out.println();

            // SmartDashboard.putString("Auton Error: ", this.autoSelectError);
        }

        if (updatingAutoMode) {
            System.out.println("----------------------------------");
        }

        // step we are currently modifying
        SmartDashboard.putNumber("12_SETTING AUTON STEP: ", this.curAutonStepToSet + 1);

        // delay
       
        SmartDashboard.putNumber("12_Auton Delay: ", this.autonDelay);

    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    public void setOverrideAuto(boolean overrideAuto) {
        this.overrideAuto = overrideAuto;
    }

}
