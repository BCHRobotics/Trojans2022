
package frc.commands;

public abstract class Command {
    // Called just before this Command runs the first time
    public abstract void initialize();

    // Called repeatedly when this Command is scheduled to run
    public abstract void calculate();

    // Called repeatedly to execute "runCycle" commands
    public abstract void execute();

    // Called once after isFinished returns true
    public abstract void end();

    // Returns true when command is over
    public abstract boolean isFinished();

    // Disables all realted subsytems
    public abstract void disable();
}
