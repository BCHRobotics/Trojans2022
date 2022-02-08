package frc.auto;

public class AutoBuilder {
    private static AutoBuilder instance;

    public static AutoBuilder getInstance() {
        if (instance == null) {
            instance = new AutoBuilder();
        }
        return instance;
    }

    
}
