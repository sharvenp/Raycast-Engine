package app.engine.core.components;

import java.awt.*;

public class Light extends Behaviour {

    public Color color;

    public double radius;
    public double intensity;

    public Light(Color color, double radius, double intensity) {
        this.color = color;
        this.radius = radius;
        this.intensity = intensity;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void update() {

    }
}
