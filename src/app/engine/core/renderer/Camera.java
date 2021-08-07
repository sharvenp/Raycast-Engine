package app.engine.core.renderer;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;

public abstract class Camera {

    public static Camera main;

    protected final Scene gameStage;
    protected final ImageView renderView;

    public Camera(Scene gameScene, ImageView renderView) {
        this.gameStage = gameScene;
        this.renderView = renderView;
    }

    public abstract void initializeScreen();

    public abstract void update();
}
