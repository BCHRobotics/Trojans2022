package frc.auton.util;

import frc.auton.AutonCommand;
import frc.auton.AutonControl;
import frc.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonStop extends AutonCommand {

	public AutonStop() {
		super(RobotComponent.UTIL);
	}

	@Override
	public void firstCycle() {
		// nothing
	}

	@Override
	public boolean calculate() {
		AutonControl.getInstance().stop();
		return true;
	}

	@Override
	public void override() {
		// nothing to do

	}

}
