package app.engine.core.renderer;

import app.assets.GameSettings;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import java.nio.IntBuffer;

public abstract class Camera {

    public static Camera main;

    protected final Scene gameStage;
    protected final ImageView renderView;
    protected IntBuffer renderBuffer;

    public Camera(Scene gameScene, ImageView renderView) {
        this.gameStage = gameScene;
        this.renderView = renderView;

        this.renderBuffer = IntBuffer.allocate(GameSettings.VIEW_WIDTH * GameSettings.VIEW_HEIGHT);
    }

    public abstract void initializeScreen();

    public abstract void update();
}
