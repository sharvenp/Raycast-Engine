package app.engine.core.renderer;

import app.assets.GameSettings;
import app.assets.levels.Level;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.renderer.camera.Camera;
import app.engine.core.renderer.camera.CameraTransform;
import app.engine.core.texture.Texture;
import app.engine.core.texture.TextureLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class RaycastRenderer {

    private static Scene gameScene;
    private static ImageView renderView;

    public static void initialize(Scene newGameScene, ImageView newRenderView) {
        gameScene = newGameScene;
        gameScene.setCursor(Cursor.NONE);
        renderView = newRenderView;
    }

    public static void render() {

        Level level = Game.getInstance().level;
        
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
        int numThreads = 5;
        Thread[] threads = new Thread[numThreads];
        int interval = GameSettings.VIEW_WIDTH / numThreads;

        for (int t = 0; t < numThreads; t++) {

            int finalT = t;
            Thread thread = new Thread() {

                public void run() {

                    // render raycasts
                    // source: https://www.instructables.com/Making-a-Basic-3D-Engine-in-Java/
                    for (int x = finalT * interval; x < (finalT + 1) * interval; x++) {

                        double cameraX = 2 * x / (double) (GameSettings.VIEW_WIDTH) - 1;
                        double rayDirX = Camera.main.transform.lookDirection.x + ((CameraTransform)Camera.main.transform).xPlane * cameraX;
                        double rayDirY = Camera.main.transform.lookDirection.y + ((CameraTransform)Camera.main.transform).yPlane * cameraX;

                        int mapX = (int) Camera.main.transform.position.x;
                        int mapY = (int) Camera.main.transform.position.y;

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
                            sideDistX = (Camera.main.transform.position.x - mapX) * deltaDistX;
                        } else {
                            stepX = 1;
                            sideDistX = (mapX + 1.0 - Camera.main.transform.position.x) * deltaDistX;
                        }
                        if (rayDirY < 0) {
                            stepY = -1;
                            sideDistY = (Camera.main.transform.position.y - mapY) * deltaDistY;
                        } else {
                            stepY = 1;
                            sideDistY = (mapY + 1.0 - Camera.main.transform.position.y) * deltaDistY;
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

                            if (level.map[mapX][mapY] > 0) {
                                hit = true;
                            }
                        }

                        if (side == 0) {
                            perpWallDist = Mathf.abs((mapX - Camera.main.transform.position.x + (1d - stepX) / 2d) / rayDirX);
                        } else {
                            perpWallDist = Mathf.abs((mapY - Camera.main.transform.position.y + (1d - stepY) / 2d) / rayDirY);
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
                        Texture tex = textures.get(texNum);

                        double wallX;
                        if (side == 1) {
                            wallX = (Camera.main.transform.position.x + ((mapY - Camera.main.transform.position.y + (1d - stepY) / 2d) / rayDirY) * rayDirX);
                        } else {
                            wallX = (Camera.main.transform.position.y + ((mapX - Camera.main.transform.position.x + (1d - stepX) / 2d) / rayDirX) * rayDirY);
                        }

                        wallX -= Math.floor(wallX);

                        int texX = (int) (wallX * (tex.resolution));

                        if (side == 0 && rayDirX > 0) {
                            texX = tex.resolution - texX - 1;
                        }

                        if (side == 1 && rayDirY < 0) {
                            texX = tex.resolution - texX - 1;
                        }

                        // draw the texture
                        for (int y = drawStart; y < drawEnd; y++) {
                            int texY;

                            if ((tex.resolution & (tex.resolution - 1)) == 0) {
                                texY = (((y * 2 - GameSettings.VIEW_HEIGHT + lineHeight) << tex.resolutionExponent) / lineHeight) / 2;
                            } else {
                                texY = (((y * 2 - GameSettings.VIEW_HEIGHT + lineHeight) / tex.resolution) / lineHeight) / 2;
                            }

                            int color = tex.pixels[texX + (texY * tex.resolution)];
                            pixels[x + y * GameSettings.VIEW_WIDTH] = color;
                        }
                    }
                }
            };

            threads[t] = thread;
            thread.start();
        }

        for (int t = 0; t < numThreads; t++) {
            try {
                threads[t].join();
            } catch (Exception ignored) { }
        }

        // update the view buffer
        renderView.setImage(image);
        pixelBuffer.updateBuffer(b -> null);
    }
}
