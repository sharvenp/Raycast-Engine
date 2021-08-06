package app.assets.camera;

import app.assets.GameSettings;
import app.assets.levels.Level;
import app.engine.core.components.GameObject;
import app.assets.player.Player;
import app.engine.core.debug.Debug;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.renderer.Camera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RaycastCamera extends Camera {

    public double xPlane;
    public double yPlane;

    public RaycastCamera(Scene gameScene, Canvas gameCanvas) {
        super(gameScene, gameCanvas);

        xPlane = 0;
        yPlane = -0.66;
    }

    @Override
    public void initializeScreen() {
        this.clearScreen();
    }

    @Override
    public void update() {
        this.clearScreen();
        this.renderLevel();
    }

    private void clearScreen() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());
    }

    private void renderLevel() {
        Player player = (Player) GameObject.findObjectWithTag("PLAYER");
        Level map = Game.getInstance().map;
        RaycastCamera camera = (RaycastCamera) Camera.main;

        if (player == null) {
            Debug.error("PLAYER IS NULL");
            return;
        }

        // Draw ceiling and floor
        gc.setFill(GameSettings.CEILING_COLOR);
        gc.fillRect(0, 0, GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT / 2d);
        gc.setFill(GameSettings.FLOOR_COLOR);
        gc.fillRect(0, GameSettings.VIEW_HEIGHT / 2d, GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT / 2d);

        // render raycasts
        // source: https://www.instructables.com/Making-a-Basic-3D-Engine-in-Java/
        for (int x = 0; x < GameSettings.VIEW_WIDTH; x++) {

            double cameraX = 2 * x / (double) (GameSettings.VIEW_WIDTH) - 1;
            double rayDirX = player.transform.lookDirection.x + camera.xPlane * cameraX;
            double rayDirY = player.transform.lookDirection.y + camera.yPlane * cameraX;

            int mapX = (int) player.transform.position.x;
            int mapY = (int) player.transform.position.y;

            double sideDistX;
            double sideDistY;

            double deltaDistX = Mathf.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Mathf.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist;

            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side = 0;//was the wall vertical or horizontal

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.transform.position.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.transform.position.x) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.transform.position.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.transform.position.y) * deltaDistY;
            }

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                if (map.map[mapX][mapY] > 0) hit = true;
            }

            if (side == 0) {
                perpWallDist = Mathf.abs((mapX - player.transform.position.x + (1d - stepX) / 2d) / rayDirX);
            } else {
                perpWallDist = Mathf.abs((mapY - player.transform.position.y + (1d - stepY) / 2d) / rayDirY);
            }

            int lineHeight;
            if (perpWallDist > 0) {
                lineHeight = (int) Mathf.abs((GameSettings.VIEW_HEIGHT / perpWallDist));
            } else {
                lineHeight = GameSettings.VIEW_HEIGHT;
            }

            int drawStart = -lineHeight / 2 + GameSettings.VIEW_HEIGHT / 2;
            if (drawStart < 0) {
                drawStart = 0;
            }

            int drawEnd = lineHeight / 2 + GameSettings.VIEW_HEIGHT / 2;
            if (drawEnd >= GameSettings.VIEW_HEIGHT) {
                drawEnd = GameSettings.VIEW_HEIGHT - 1;
            }

            gc.setStroke(Color.color(Mathf.clamp((double) lineHeight / GameSettings.VIEW_HEIGHT, 0, 1), 0, 0));
            gc.strokeLine(x, drawStart, x, drawEnd);
        }

    }
}
