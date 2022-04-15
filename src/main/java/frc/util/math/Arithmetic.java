package frc.util.math;

import java.lang.Math;

public class Arithmetic {
    public static double constrain(double input, double min, double max) {
        return Math.min(Math.max(input, min), max);
    }
}
