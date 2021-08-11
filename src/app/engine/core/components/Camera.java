package app.engine.core.components;

public class Camera extends GameObject {

    public static Camera main;

    public Light light;

    public Camera() {
        super();
        type = DefaultComponentType.CAMERA.getType();
        transform = new CameraTransform();
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void update() {

    }
}
