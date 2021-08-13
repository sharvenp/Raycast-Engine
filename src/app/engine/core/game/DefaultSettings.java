package app.engine.core.game;

import app.engine.core.texture.Texture;

import java.awt.*;

public abstract class DefaultSettings {

    public static boolean DEBUG_MODE;

    public static int VIEW_WIDTH;
    public static int VIEW_HEIGHT;

    public static int TEXTURE_RESOLUTION;

    public static boolean ENABLE_LIGHT;

    public static Color DEFAULT_CEILING_COLOR;
    public static Color DEFAULT_FLOOR_COLOR;
    public static Color DEFAULT_WALL_COLOR;

    public static Texture FLOOR_TEXTURE;
    public static Texture CEIL_TEXTURE;

    public static void initialize() {

        DEBUG_MODE = false;

        VIEW_WIDTH = 640;
        VIEW_HEIGHT = 480;
        TEXTURE_RESOLUTION = 32;

        ENABLE_LIGHT = false;

        DEFAULT_CEILING_COLOR = new Color(10, 10, 40, 255);
        DEFAULT_FLOOR_COLOR = new Color(50, 10, 10, 255);
        DEFAULT_WALL_COLOR = new Color(250, 50, 50, 255);
    }

}
