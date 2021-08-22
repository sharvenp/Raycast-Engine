package app.engine.core.sprite;

public class Sprite {
    public int[] pixels;
    public int width;
    public int height;

    public Sprite(int w, int h) {
        this.width = w;
        this.height = h;
        this.pixels = new int[w * h];
    }
}
