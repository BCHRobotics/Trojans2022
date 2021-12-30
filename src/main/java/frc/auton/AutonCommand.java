package frc.auton;

public abstract class AutonCommand {
    private static AutonCommand[] autonComponents = new AutonCommand[RobotComponent.values().length];

    private RobotComponent cmdType;

    private long timeout = -1;
    private long startTime = -1;
    private boolean isFirstCycle = true;

    public AutonCommand(RobotComponent type, long timeoutLength) {
        this.cmdType = type;
        this.timeout = timeoutLength;
    }

    public AutonCommand(RobotComponent type) {
        this(type, -1);
    }

    public abstract void firstCycle();

    public abstract boolean calculate();

    public abstract void override();

    public boolean checkAndRun() {
		// is something already running for this component
		if (autonComponents[this.cmdType.ordinal()] == null) {
			// if not, start running
			autonComponents[this.cmdType.ordinal()] = this;
			return true;
		} else {
			return false;
		}
	}

	public boolean runCommand() {
		if (this.startTime < 0) {
			this.startTime = System.currentTimeMillis();
		}

		if (this.isFirstCycle) {
			this.firstCycle();
			this.isFirstCycle = false;
		}
		boolean done = this.calculate();
		long timePassed = System.currentTimeMillis() - this.startTime;

		if (this.timeout > 0 && timePassed > this.timeout) {
			System.out.println("TIME OUT OCCURRED " + this.getClass().getName());
			this.override();
			return true;
		} else if (done) {
			return true;
		} else {
			return false;
		}
	}

	public static void execute() {
		for (int i = 0; i < autonComponents.length; i++) {
			// if that component has a command running
			if (autonComponents[i] != null) {
				// run command

				boolean done = autonComponents[i].runCommand();
				// finished, so remove
				if (done) {
					autonComponents[i] = null;
				}
			}
		}
	}

	public static void overrideComponent(RobotComponent typeToOverride) {
		if (autonComponents[typeToOverride.ordinal()] != null) {
			autonComponents[typeToOverride.ordinal()].override();
		}
		autonComponents[typeToOverride.ordinal()] = null;
	}

	public static void reset() {
		for (int i = 0; i < autonComponents.length; i++) {
			autonComponents[i] = null;
		}
	} 
}
