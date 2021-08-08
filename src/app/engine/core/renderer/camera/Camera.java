package app.engine.core.renderer.camera;

import app.engine.core.components.GameObject;

public class Camera extends GameObject {

    public static Camera main;

    public Camera() {
        super();
        tag = "CAMERA";
        transform = new CameraTransform();
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void update() {

    }
}
