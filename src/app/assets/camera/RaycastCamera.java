package app.assets.camera;

import app.engine.core.components.GameObject;
import app.assets.player.Player;
import app.engine.core.renderer.Camera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RaycastCamera extends Camera {

    public RaycastCamera(Scene gameScene, Canvas gameCanvas) {
        super(gameScene, gameCanvas);
    }

    @Override
    public void initializeScreen() {
        this.clearScreen();
    }

    @Override
    public void update() {
        this.clearScreen();

        double interval = this.gameCanvas.getWidth() / 80;
        this.renderWalls();
    }

    private void clearScreen() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());
    }

    private void renderWalls() {
        Player player = (Player) GameObject.findObjectWithTag("PLAYER");

        if (player == null) {
            System.out.println("PLAYER IS NULL");
            return;
        }

        // TODO: Raycast renderer!
    }
}
