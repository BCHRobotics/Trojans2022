package frc.auto;

public class AutoSelecter {
    private static AutoSelecter instance;

    public static AutoSelecter getInstance() {
        if (instance == null) {
            instance = new AutoSelecter();
        }
        return instance;
    }
}
