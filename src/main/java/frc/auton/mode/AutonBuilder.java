
package frc.auton.mode;

import java.util.ArrayList;

import frc.auton.AutonCommand;
import frc.auton.util.AutonStop;

public class AutonBuilder {

	private ArrayList<AutonCommand> cmds;

	public AutonBuilder() {
		this.cmds = new ArrayList<>();
	}

	public void addCommand(AutonCommand cmd) {
		this.cmds.add(cmd);
	}

	public void addMode(AutonMode mode) {
		mode.addToMode(this);
	}

	public AutonCommand[] getAutonList() {
		// add a stop at the end of every auton mode
		this.cmds.add(new AutonStop());

		AutonCommand[] result = new AutonCommand[this.cmds.size()];
		result = this.cmds.toArray(result);

		return result;
	}

}
