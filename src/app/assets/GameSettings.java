package app.assets;

import app.engine.core.game.Settings;

import java.awt.*;

public class GameSettings extends Settings {

    // color palette
    public static Color FLOOR_COLOR;
    public static Color CEILING_COLOR;

    public static void initialize() {
        VIEW_WIDTH = 1280;
        VIEW_HEIGHT = 720;

        FLOOR_COLOR = new Color(100, 100, 100);
        CEILING_COLOR = new Color(	135, 206, 235);
    }

}
