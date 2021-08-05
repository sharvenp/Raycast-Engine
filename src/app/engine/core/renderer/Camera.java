package app.engine.core.renderer;

import app.engine.core.components.GameObject;
import app.engine.core.game.Game;
import app.assets.player.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Camera {

    protected final Scene gameStage;
    protected final Canvas gameCanvas;
    protected GraphicsContext graphicsContext;

    public Camera(Scene gameScene, Canvas gameCanvas) {
        this.gameStage = gameScene;
        this.gameCanvas = gameCanvas;
        this.graphicsContext = this.gameCanvas.getGraphicsContext2D();
    }

    public abstract void initializeScreen();

    public abstract void update();
}
