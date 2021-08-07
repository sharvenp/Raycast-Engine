package app.engine.core.texture;

import app.engine.core.debug.Debug;

public class Texture {

    public int[] pixels;
    private String path;
    public final int resolution;
    public  int resolutionExponent;

    public Texture(String path, int size) {
        this.path = path;
        this.resolutionExponent = -1;
        this.resolution = size;
        this.pixels = new int[size * size];

        if ((size & (size - 1)) == 0) {
            int currSize = size;
            int e = 0;
            while ((size >> 1) != 0) {
                size = size >> 1;
                e++;
            }
            this.resolutionExponent = e;
        }
    }
}
