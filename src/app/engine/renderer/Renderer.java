package app.engine.renderer;

import app.engine.game.Game;
import app.engine.assets.player.Player;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {

    private final Scene gameStage;
    private final Canvas gameCanvas;
    private GraphicsContext graphicsContext;

    public Renderer(Scene gameScene, Canvas gameCanvas) {
        this.gameStage = gameScene;
        this.gameCanvas = gameCanvas;
        this.graphicsContext = this.gameCanvas.getGraphicsContext2D();
    }

    public void initScreen() {
        this.clearScreen();
    }

    public void render(Game game) {
        this.clearScreen();

        double interval = this.gameCanvas.getWidth() / 80;
        this.renderWalls(game);
    }

    private void clearScreen() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());
    }

    private void renderWalls(Game game) {
        Player player = (Player) game.findObjectWithTag("PLAYER");

        if (player == null) {
            System.out.println("PLAYER IS NULL");
            return;
        }

        // TODO: Raycast renderer!
    }
}
