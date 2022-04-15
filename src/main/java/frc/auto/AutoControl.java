package frc.auto;

import java.util.ArrayList;

public class AutoControl {
    
    public ArrayList<AutoComponent> components;
    private static AutoControl instance;

    public static AutoControl getInstance() {
        if (instance == null) {
            instance = new AutoControl();
        }
        return instance;
    }

    private AutoControl() {
        this.components = new ArrayList<>();
        this.components.add(AutoOperate.getInstance());

		AutoSelecter.getInstance();
    }

    public void runCycle() {
		for (AutoComponent t : this.components) {
			t.run();
		}
	}

	public void disable() {
		for (AutoComponent t : this.components) {
			t.disable();
		}
	}

	public void initialize() {
		for (AutoComponent t : this.components) {
			t.firstCycle();
		}
	}

}
