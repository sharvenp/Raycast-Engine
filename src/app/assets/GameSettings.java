package app.assets;

import app.engine.core.game.DefaultSettings;

public class GameSettings extends DefaultSettings {

    public static void initialize() {
        DefaultSettings.initialize();
        DEBUG_MODE = true;
        TEXTURE_RESOLUTION = 64;
        ENABLE_LIGHT = true;
    }

}
