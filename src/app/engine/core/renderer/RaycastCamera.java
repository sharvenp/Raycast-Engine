package app.engine.core.renderer;

import app.assets.GameSettings;
import app.assets.levels.Level;
import app.engine.core.components.GameObject;
import app.assets.player.Player;
import app.engine.core.debug.Debug;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.texture.Texture;
import app.engine.core.texture.TextureLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class RaycastCamera extends Camera {

    public double xPlane;
    public double yPlane;

    private final int THREADS = 5;

    public RaycastCamera(Scene gameScene, ImageView renderView) {
        super(gameScene, renderView);

        gameScene.setCursor(Cursor.NONE);
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
        renderView.setImage(null);
    }

    private void renderLevel() {

        Level level = Game.getInstance().map;
        
        Player player = (Player) GameObject.findObjectWithTag("PLAYER");
        if (player == null) {
            Debug.error("PLAYER IS NULL");
            return;
        }

        ArrayList<Texture> textures = TextureLoader.getTextures();
        if (textures == null || textures.size() == 0) {
            return;
        }

        // start the rendering

        IntBuffer renderBuffer = IntBuffer.allocate(GameSettings.VIEW_WIDTH * GameSettings.VIEW_HEIGHT);
        int[] pixels = renderBuffer.array();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT, renderBuffer, PixelFormat.getIntArgbPreInstance());
        WritableImage image = new WritableImage(pixelBuffer);

        // Draw ceiling and floor
        for (int i = 0; i < pixels.length / 2; i++) {
            pixels[i] = GameSettings.CEILING_COLOR.getRGB();
            pixels[i + pixels.length / 2] = GameSettings.FLOOR_COLOR.getRGB();
        }

        // multi-threaded raycast rendering
        Thread[] threads = new Thread[THREADS];
        RaycastCamera cam = this;
        int interval = GameSettings.VIEW_WIDTH / THREADS;

        for (int t = 0; t < THREADS; t++) {

            int finalT = t;
            Thread thread = new Thread() {

                public void run() {

                    // render raycasts
                    // source: https://www.instructables.com/Making-a-Basic-3D-Engine-in-Java/
                    for (int x = finalT * interval; x < (finalT + 1) * interval; x++) {

                        double cameraX = 2 * x / (double) (GameSettings.VIEW_WIDTH) - 1;
                        double rayDirX = player.transform.lookDirection.x + cam.xPlane * cameraX;
                        double rayDirY = player.transform.lookDirection.y + cam.yPlane * cameraX;

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

                            if (level.map[mapX][mapY] > 0) hit = true;
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

                        int texNum = level.map[mapX][mapY];

                        double wallX;
                        if (side == 1) {
                            wallX = (player.transform.position.x + ((mapY - player.transform.position.y + (1d - stepY) / 2d) / rayDirY) * rayDirX);
                        } else {
                            wallX = (player.transform.position.y + ((mapX - player.transform.position.x + (1d - stepX) / 2d) / rayDirX) * rayDirY);
                        }

                        wallX -= Math.floor(wallX);

                        int texX = (int) (wallX * (textures.get(texNum).size));

                        if (side == 0 && rayDirX > 0) {
                            texX = textures.get(texNum).size - texX - 1;
                        }

                        if (side == 1 && rayDirY < 0) {
                            texX = textures.get(texNum).size - texX - 1;
                        }

                        for (int y = drawStart; y < drawEnd; y++) {
                            int texY = (((y * 2 - GameSettings.VIEW_HEIGHT + lineHeight) << 6) / lineHeight) / 2;
                            int color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).size)];
                            pixels[x + y * GameSettings.VIEW_WIDTH] = color;
                        }
                    }
                }
            };

            threads[t] = thread;
            thread.start();
        }

        for (int t = 0; t < THREADS; t++) {
            try {
                threads[t].join();
            } catch (Exception ignored) { }
        }

        // update the view buffer

        renderView.setImage(image);
        pixelBuffer.updateBuffer(b -> null);
    }
}
