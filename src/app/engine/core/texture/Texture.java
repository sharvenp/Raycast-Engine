package app.engine.core.texture;

public class Texture {

    public int[] pixels;
    private String path;
    public final int size;

    public Texture(String path, int size) {
        this.path = path;
        this.size = size;
        this.pixels = new int[size * size];
    }
}
