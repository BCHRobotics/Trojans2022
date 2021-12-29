package frc.subsystems;

public abstract class Subsystem {
    
    // Create an abstract class that initializes everything in the subsystem
	public abstract void firstCycle();

	// calculates (do everything)
	public abstract void calculate();

	// disables
	public abstract void disable();

}
