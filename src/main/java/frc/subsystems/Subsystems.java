package frc.subsystems;

public class Subsystems {
    private static Subsystems instance;
    
    public final Subsystem[] subsystems = new Subsystem[] {
        Climber.getInstance(),
        Drivetrain.getInstance(),
        Intake.getInstance(),
        Shooter.getInstance()
    };

    public static Subsystems getInstance() {
        if (instance == null) instance = new Subsystems();
        return instance;
    }

    public void firstCycle() {
        for (Subsystem s : this.subsystems) {
            s.firstCycle();
        }
    }

    public void run() {
        for (Subsystem s : this.subsystems) {
            s.run();
        }
    }

    public void disable() {
        for (Subsystem s : this.subsystems) {
            s.disable();
        }
    }

}
