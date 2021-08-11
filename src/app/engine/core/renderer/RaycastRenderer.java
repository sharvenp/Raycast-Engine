package app.engine.core.renderer;

import app.assets.GameSettings;
import app.assets.levels.*;
import app.engine.core.components.*;
import app.engine.core.debug.Debug;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.math.Vector2;
import app.engine.core.math.Vector3;
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

        Level0 level = Game.getInstance().level;

        ArrayList<Texture> textures = TextureLoader.getTextures();
        if (textures == null || textures.size() == 0) {
            return;
        }

        Light light = (Light) Camera.main.findBehaviour(Light.class.toString());

        // start the rendering
        IntBuffer renderBuffer = IntBuffer.allocate(GameSettings.VIEW_WIDTH * GameSettings.VIEW_HEIGHT);
        int[] pixels = renderBuffer.array();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT, renderBuffer, PixelFormat.getIntArgbPreInstance());
        WritableImage image = new WritableImage(pixelBuffer);

        // Draw ceiling and floor
        //FLOOR CASTING
        Texture floorTex = textures.get(0);
        for(int y = GameSettings.VIEW_HEIGHT / 2; y < GameSettings.VIEW_HEIGHT; y++)
        {
            double rayDirX0 = Camera.main.transform.lookDirection.x - ((CameraTransform)Camera.main.transform).xPlane;
            double rayDirY0 = Camera.main.transform.lookDirection.y - ((CameraTransform)Camera.main.transform).yPlane;
            double rayDirX1 = Camera.main.transform.lookDirection.x + ((CameraTransform)Camera.main.transform).xPlane;
            double rayDirY1 = Camera.main.transform.lookDirection.y + ((CameraTransform)Camera.main.transform).yPlane;

            int p = y - GameSettings.VIEW_HEIGHT / 2;
            double posZ = 0.5 * GameSettings.VIEW_HEIGHT;
            double rowDistance = posZ / p;

            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / GameSettings.VIEW_WIDTH;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / GameSettings.VIEW_WIDTH;

            double floorX = Camera.main.transform.position.x + rowDistance * rayDirX0;
            double floorY = Camera.main.transform.position.y + rowDistance * rayDirY0;

            for(int x = 0; x < GameSettings.VIEW_WIDTH; ++x)
            {
                int cellX = (int)(floorX);
                int cellY = (int)(floorY);

                // get the texture coordinate from the fractional part
                int tx = (int)(floorTex.resolution * (floorX - cellX)) & (floorTex.resolution - 1);
                int ty = (int)(floorTex.resolution * (floorY - cellY)) & (floorTex.resolution - 1);

                floorX += floorStepX;
                floorY += floorStepY;

                // FAST INVERSE SQUARE
                double distance = (new Vector2(floorX, floorY)).squareDistance(Camera.main.transform.position);
                double xhalf = 0.5d * distance;
                long i = Double.doubleToLongBits(distance);
                i = 0x5fe6ec85e7de30daL - (i >> 1);
                distance = Double.longBitsToDouble(i);
                distance *= (1.5d - xhalf * distance * distance);
                distance = 1 / distance;

                int color = floorTex.pixels[floorTex.resolution * ty + tx];
                if (GameSettings.LIGHT_ENABLED && light != null) {
                    double scale = Mathf.clamp(light.radius / (distance > 0 ? distance : 1), 0, 1);
                    color = calculateLightPixelColor(light, color, scale);
                }
                pixels[x + y * GameSettings.VIEW_WIDTH] = color;
                pixels[x + (GameSettings.VIEW_HEIGHT - y - 1) * GameSettings.VIEW_WIDTH] = GameSettings.CEILING_COLOR.getRGB();
            }
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

                            if (GameSettings.LIGHT_ENABLED && light != null) {
                                double scale = Mathf.clamp(light.radius / (perpWallDist > 0 ? perpWallDist : 1), 0, 1);
                                color = calculateLightPixelColor(light, color, scale);
                            }
                            pixels[x  + y * GameSettings.VIEW_WIDTH] = (int) color;
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

    private static int calculateLightPixelColor(Light light, int pixelColor, double scale) {
        Vector3 color2 = new Vector3((pixelColor >> 16) & 0xFF, (pixelColor >> 8) & 0xFF, pixelColor & 0xFF);
        Vector3 color1 = new Vector3((light.color.getRed() + color2.x) / 2, (light.color.getGreen() + color2.y) / 2, (light.color.getBlue() + color2.z) / 2);
        Vector3 lerpedColor = Mathf.lerp(color1, color2, scale).scale(scale);
        int rgb = 0xFF;
        rgb = (rgb << 8) + (int) Mathf.clamp( lerpedColor.x, 0, 255);
        rgb = (rgb << 8) + (int) Mathf.clamp( lerpedColor.y, 0, 255);
        rgb = (rgb << 8) + (int) Mathf.clamp( lerpedColor.z, 0, 255);
        return rgb;
    }

}
