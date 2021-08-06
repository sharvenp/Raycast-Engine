package app.assets;

import app.engine.core.game.Settings;
import javafx.scene.paint.Color;

public class GameSettings extends Settings {

    // color palette
    public static Color FLOOR_COLOR;
    public static Color CEILING_COLOR;

    static {
        VIEW_WIDTH = 1280;
        VIEW_HEIGHT = 720;

        DEBUG_LOG = true;
        DEBUG_ERROR = true;
        DEBUG_WARN = false;

        FLOOR_COLOR = Color.BLACK;
        CEILING_COLOR = Color.GRAY;
    }

}
