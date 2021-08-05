package app.engine.assets;

import app.engine.math.Vector3;

public class Transform {

    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    public Transform() {
        this.position = new Vector3();
        this.rotation = new Vector3();
        this.scale = new Vector3();
    }
}
