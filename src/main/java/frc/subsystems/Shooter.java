package frc.subsystems;

import javax.swing.text.Position;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.io.Input.SensorInput;
import frc.io.Output.RobotOutput;

public class Shooter extends Subsystem {
    private static Shooter instance;

    private RobotOutput robotOutput;
    private SensorInput sensorInput;

    private double shooterWheelSpeed;
    private double shooterTurretPosition;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    private Shooter() {
        this.robotOutput = RobotOutput.getInstance();
        this.sensorInput = SensorInput.getInstance();

        firstCycle();
    }

    @Override
    public void firstCycle() {

    }

    @Override
    public void calculate() {
        this.robotOutput.shooter.setWheelSpeed(shooterWheelSpeed);
        this.robotOutput.shooter.setTurretPosition(shooterTurretPosition);
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        this.robotOutput.shooter.stopAll();
    }

    public void setShooterWheelSpeed(double speed) {
        this.shooterWheelSpeed = speed;
    }

    public void setShooterTurretPosition(double position) {
        this.shooterTurretPosition = position;
    }
    
}
