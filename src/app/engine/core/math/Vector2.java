package app.engine.core.math;

public class Vector2 extends Vector3 {

    public Vector2(double x, double y) {
        super(x, y, 0);
    }

    public Vector2(Vector2 other) {
        this.set(other);
    }

    public Vector2() { super(); }

    public static Vector2 forward() { return new Vector2(1, 0); }
    public static Vector2 left() { return new Vector2(0, 1); }
}
