package frc.io.subsystems;

import java.util.ArrayList;

public class IO {

    public ArrayList<IIO> subsystems;
    private static IO instance;

    public static IO getInstance() {
        if(instance == null) instance = new IO();
        return instance;
    }

    private IO() {
        this.subsystems = new ArrayList<>();

        this.subsystems.add(DriveIO.getInstance());
    }

    public void updateInputs() {
        for (IIO io : this.subsystems) {
            io.updateInputs();
        }
    }

    public void resetInputs() {
        for (IIO io : subsystems) {
            io.resetInputs();
        }
    }

    public void stopAllOutputs() {
        for (IIO io : subsystems) {
            io.stopAllOutputs();
        }
    }

}
