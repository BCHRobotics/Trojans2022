package frc.teleop;

import frc.io.DriverInput;
import frc.subsystems.Shooter;
import frc.subsystems.Drive;
import frc.util.devices.Controller;
import frc.util.devices.Controller.Axis;
import frc.util.devices.Controller.Side;

public class TeleopOperator extends TeleopComponent {

    
    private static TeleopOperator instance;

    private Controller operatorController;
    private DriverInput driverInput;

    private enum OperatorMode {
        DRIVE, SHOOT
    }

    private OperatorMode operatorMode = OperatorMode.SHOOT;

    private Drive drive;

    private Shooter shooter;
    /**
     * Get the instance of the TeleopOperator, if none create a new instance
     * 
     * @return instance of the TeleopOperator
     */
    public static TeleopOperator getInstance() {
        if (instance == null) {
            instance = new TeleopOperator();
        }
        return instance;
    }



    private TeleopOperator() {
        this.shooter = Shooter.getInstance();
        this.driverInput = DriverInput.getInstance();


        this.driverInput = DriverInput.getInstance();
        this.drive = Drive.getInstance();
        this.operatorController = driverInput.getDriverController();

    }


    @Override
    public void firstCycle() {
        this.operatorController = driverInput.getOperatorController();
        this.shooter.firstCycle();

        this.drive.firstCycle();
    }

    @Override
    public void calculate() {

        System.out.println("Teleop Operator Calculate!");

        switch (operatorMode) {
            case SHOOT:
                shootMode();
                break;
            case DRIVE:
            default:
                driveMode();
                break;
        }

        if (operatorController.getModeSwitchButtonsPressed()) {
            if (operatorMode == OperatorMode.DRIVE) operatorMode = OperatorMode.SHOOT;
            if (operatorMode == OperatorMode.SHOOT) operatorMode = OperatorMode.DRIVE;
        }

        double speed = 0.75;

        if (operatorController.getLeftBumper()) speed = 0.5;
        if (operatorController.getRightBumper()) speed = 1.0;

        drive.setOutput(
            operatorController.getJoystick(Side.RIGHT, Axis.Y) * speed, 
            operatorController.getJoystick(Side.RIGHT, Axis.X) * speed
        );

        this.drive.calculate();

        shooter.calculate();
    }

    /**
     * Climb mode for operator controller
     */
    private void shootMode() {

        System.out.println("Shoot Mode!");

        double speed = 6000;
        double rotations = 60;
        
        shooter.setShooterWheelSpeed(operatorController.getJoystick(Side.LEFT, Axis.Y) * speed);
        shooter.setShooterArmPosition(operatorController.getJoystick(Side.RIGHT, Axis.Y) * rotations);

    }

    /**
     * Drive mode for operator controller
     */
    private void driveMode() {
        System.out.println("Drive Mode!");


    }

    @Override
    public void disable(){
        this.drive.disable();
        shooter.disable();
    }
}