package app.engine.core.math;

public class Mathf {

    public static double PI;

    static {
        PI = Math.PI;
    }

    public static double cos(double rad) {
        return Math.cos(rad);
    }

    public static double sin(double rad) {
        return Math.sin(rad);
    }

    public static Vector3 lerp(Vector3 a, Vector3 b, double t) {
        return a.scale(t).add(b.scale(1 - t));
    }

    public static double abs(double val) { return Math.abs(val); }

    public static double sqrt(double val) { return Math.sqrt(val); }

    public static double clamp(double value, double min, double max) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        }
        return value;
    }
}
