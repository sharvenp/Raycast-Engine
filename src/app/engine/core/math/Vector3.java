package app.engine.core.math;

public class Vector3 {

    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 other) {
        this.set(other);
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public static Vector3 zero() { return new Vector3(); }
    public static Vector3 forward() { return new Vector3(1, 0, 0); }
    public static Vector3 left() { return new Vector3(0, 1, 0); }
    public static Vector3 up() { return new Vector3(0, 0, 1); }

    public Vector3 set(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;

        return this;
    }

    public Vector3 add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;

        return this;
    }

    public Vector3 subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;

        return this;
    }

    public Vector3 scale(double scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;

        return this;
    }

    public Vector3 flip() {
        return this.scale(-1);
    }

    public double magnitude() {
        return Math.sqrt(this.squareMagnitude());
    }

    public double squareMagnitude() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    public double distance(Vector3 to) {
        return Math.sqrt((this.x - to.x) * (this.x - to.x) +
                         (this.y - to.y) * (this.y - to.y) +
                         (this.z - to.z) * (this.z - to.z));
    }

    public double squareDistance(Vector3 to) {
        return (this.x - to.x) * (this.x - to.x) + (this.y - to.y) * (this.y - to.y) + (this.z - to.z) * (this.z - to.z);
    }

    public Vector3 normalize() {
        double magnitude = this.magnitude();
        if (magnitude == 0) {
            return this;
        }
        return this.scale(1/magnitude);
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + ", " + z + '>';
    }
}
