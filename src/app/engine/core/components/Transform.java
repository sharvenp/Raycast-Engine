package app.engine.core.components;

import app.engine.core.math.Vector2;
import app.engine.core.math.Vector3;

public class Transform {

    public Vector3 position;
    public Vector2 lookDirection;
    public Vector3 scale;

    public Transform() {
        this.position = new Vector3();
        this.lookDirection = new Vector2();
        this.scale = new Vector3();
    }
}
