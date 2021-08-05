package app.engine.core.math;

public class Vector3 {

    public static Vector3 zero;
    public static Vector3 forward;
    public static Vector3 left;
    public static Vector3 up;

    static {
        zero = new Vector3();
        forward = new Vector3(1, 0, 0);
        left = new Vector3(0, 1, 0);
        up = new Vector3(0, 0, 1);
    }

    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void set(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void scale(double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
    }

    public void flip() {
        this.scale(-1);
    }

    public double magnitude() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public double squareMagnitude() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public void normalize() {
        double magnitude = this.magnitude();
        if (magnitude == 0) {
            return;
        }
        this.scale(1/magnitude);
    }

}
