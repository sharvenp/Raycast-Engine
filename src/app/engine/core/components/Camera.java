package app.engine.core.components;

public class Camera extends GameObject {

    public static Camera main;

    public Camera() {
        super();
        tag = "CAMERA_MAIN";
        transform = new CameraTransform();
    }
}
