package app.engine.core.components;

import java.awt.*;

public class Light {

    public Color color;
    public double radius;
    public double intensity;

    public Light(Color color, double radius, double intensity) {
        this.color = color;
        this.radius = radius;
        this.intensity = intensity;
    }

}
