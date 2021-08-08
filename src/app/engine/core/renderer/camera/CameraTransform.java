package app.engine.core.renderer.camera;

import app.engine.core.components.Transform;
import app.engine.core.math.Mathf;

public class CameraTransform extends Transform {

    public double xPlane;
    public double yPlane;

    public CameraTransform() {
        super();
        xPlane =  0;
        yPlane = -0.66;
    }

    @Override
    public void rotate(double rotSpeed) {
        super.rotate(rotSpeed);

        double oldXPlane = xPlane;
        xPlane = xPlane * Mathf.cos(rotSpeed) - yPlane * Mathf.sin(rotSpeed);
        yPlane = oldXPlane * Mathf.sin(rotSpeed) + yPlane * Mathf.cos(rotSpeed);
    }

}
