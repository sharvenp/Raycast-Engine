package app.engine.core.components;

import java.awt.*;

public class Light extends GameObject {

    public Color color;

    public double radius;
    public double intensity;
    public double fallOffIntensity;

    public Light(Color color, double radius, double intensity, double fallOffIntensity) {
        type = DefaultComponentType.LIGHT.getType();

        this.color = color;
        this.radius = radius;
        this.intensity = intensity;
        this.fallOffIntensity = fallOffIntensity;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void update() {

    }
}
