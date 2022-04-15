package frc.subsystems;

public abstract class Subsystem {
    
    // Create an abstract class that initializes everything in the subsystem
	public abstract void firstCycle();

	// runs the subsytem (does everything)
	public abstract void run();

	// disables
	public abstract void disable();

}
