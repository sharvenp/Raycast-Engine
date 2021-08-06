package app.engine.core.renderer;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Camera {

    public static Camera main;

    protected final Scene gameStage;
    protected final Canvas gameCanvas;
    protected GraphicsContext gc;

    public Camera(Scene gameScene, Canvas gameCanvas) {
        this.gameStage = gameScene;
        this.gameCanvas = gameCanvas;
        this.gc = this.gameCanvas.getGraphicsContext2D();
    }

    public abstract void initializeScreen();

    public abstract void update();
}
