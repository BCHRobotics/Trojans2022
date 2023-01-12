package frc.commands;

import frc.subsystems.Subsystems;

public abstract class Command {
    private Subsystems subsystems = Subsystems.getInstance();
    protected boolean isFinished;

    private OnInitialize onInitialize;
    private OnExecute onExecute;
    private OnEnd onEnd;
    private OnDisable onDisable;

    public interface OnInitialize {
        void onInitialize();
    }

    public interface OnExecute {
        void onExecute();
    }

    public interface OnEnd {
        void onEnd();
    }

    public interface OnDisable {
        void onDisable();
    }

    /**
     * This method is called just before this Command runs for the first time.
     */
    public final void initialize() {
        subsystems.firstCycle();
        isFinished = false;

        if (this.onInitialize != null) this.onInitialize.onInitialize();
    }

    /**
     * Set the method to run when the Command initializes. No need to run first cycle for
     * any of the subsystems, this is already handled.
     * @param onInitialize method to run when {@link #initialize()} is called.
     */
    protected final void setOnInitialize(OnInitialize onInitialize) {
        this.onInitialize = onInitialize;
    }

    /**
     * This Method is Called repeatedly when this Command is scheduled to run.
     */
    public abstract void calculate();

    /**
     * This Method is Called repeatedly to execute "runCycle" commands
     */
    public final void execute() {
        subsystems.run();

        if (this.onExecute != null) this.onExecute.onExecute();
    }

    /**
     * Set the method to run when the Command executes "runCycle". No need to call run for
     * any of the subsystems, this is already handled.
     * @param onExecute method to run when {@link #execute()} is called.
     */
    protected final void setOnExecute(OnExecute onExecute) {
        this.onExecute = onExecute;
    }

    /**
     * Called once after isFinished returns true
     */
    public final void end() {
        this.isFinished = true;

        if (this.onEnd != null) this.onEnd.onEnd();
    }

    /**
     * Set the method to run when the Command ends. No need to set the isFinished to true.
     * @param onEnd method to run when {@link #end()} is called.
     */
    protected final void setOnEnd(OnEnd onEnd) {
        this.onEnd = onEnd;
    }

    /**
     * Returns true when command is over
     * @return Is Finished
     */
    public final boolean isFinished() {
        return this.isFinished;
    }

    /**
     * Disables all subsystems
     */
    public final void disable() {
        subsystems.disable();

        if (this.onDisable != null) this.onDisable.onDisable();
    }

    /**
     * Set the method to run when the Command ios disabled. No need to disable the subsystems.
     * @param onDisable method to run when {@link #disable()} is called.
     */
    protected final void setOnDisable(OnDisable onDisable) {
        this.onDisable = onDisable;
    }
    
}
