package app.assets;

import app.engine.core.game.Settings;

import java.awt.*;

public class GameSettings extends Settings {

    // color palette
    public static Color CEILING_COLOR;

    public static void initialize() {
        VIEW_WIDTH = 640;
        VIEW_HEIGHT = 480;
        TEXTURE_RESOLUTION = 64;

        LIGHT_ENABLED = true;

        CEILING_COLOR = new Color(35, 35, 35);
    }

}
