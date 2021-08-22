package app.engine.core.renderer;

import app.assets.GameSettings;
import app.assets.levels.*;
import app.engine.core.components.*;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.math.Vector2;
import app.engine.core.math.Vector3;
import app.engine.core.components.SpriteRenderer;
import app.engine.core.sprite.Direction;
import app.engine.core.sprite.Sprite;
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

        Light light = Camera.main.<Light>getComponent(Light.class);

        // start the rendering
        IntBuffer renderBuffer = IntBuffer.allocate(GameSettings.VIEW_WIDTH * GameSettings.VIEW_HEIGHT);
        int[] pixels = renderBuffer.array();
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT, renderBuffer, PixelFormat.getIntArgbPreInstance());
        WritableImage image = new WritableImage(pixelBuffer);
        double[] zBuffer = new double[GameSettings.VIEW_WIDTH];

        // Draw ceiling and floor
        Texture floorTex = GameSettings.FLOOR_TEXTURE;
        Texture ceilTex = GameSettings.CEIL_TEXTURE;
        for (int y = GameSettings.VIEW_HEIGHT / 2; y < GameSettings.VIEW_HEIGHT; y++) {
            double rayDirX0 = Camera.main.transform.lookDirection.x - ((CameraTransform) Camera.main.transform).xPlane;
            double rayDirY0 = Camera.main.transform.lookDirection.y - ((CameraTransform) Camera.main.transform).yPlane;
            double rayDirX1 = Camera.main.transform.lookDirection.x + ((CameraTransform) Camera.main.transform).xPlane;
            double rayDirY1 = Camera.main.transform.lookDirection.y + ((CameraTransform) Camera.main.transform).yPlane;

            int p = y - GameSettings.VIEW_HEIGHT / 2;
            double posZ = 0.5 * GameSettings.VIEW_HEIGHT;
            double rowDistance = posZ / p;

            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / GameSettings.VIEW_WIDTH;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / GameSettings.VIEW_WIDTH;

            double floorX = Camera.main.transform.position.x + rowDistance * rayDirX0;
            double floorY = Camera.main.transform.position.y + rowDistance * rayDirY0;

            for (int x = 0; x < GameSettings.VIEW_WIDTH; ++x) {
                int cellX = (int) (floorX);
                int cellY = (int) (floorY);

                // get the texture coordinate from the fractional part
                int floorColor = GameSettings.DEFAULT_FLOOR_COLOR.getRGB();
                int ceilColor = GameSettings.DEFAULT_CEILING_COLOR.getRGB();

                if (floorTex != null) {
                    int tx = (int) (floorTex.resolution * (floorX - cellX)) & (floorTex.resolution - 1);
                    int ty = (int) (floorTex.resolution * (floorY - cellY)) & (floorTex.resolution - 1);
                    floorColor = floorTex.pixels[floorTex.resolution * ty + tx];
                }

                if (ceilTex != null) {
                    int tx = (int) (ceilTex.resolution * (floorX - cellX)) & (ceilTex.resolution - 1);
                    int ty = (int) (ceilTex.resolution * (floorY - cellY)) & (ceilTex.resolution - 1);
                    ceilColor = ceilTex.pixels[ceilTex.resolution * ty + tx];
                }

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

                if (GameSettings.ENABLE_LIGHT && light != null) {
                    double scale = Mathf.clamp(light.radius / (distance > 0 ? distance : 1), 0, 1);
                    floorColor = calculateLightPixelColor(light, floorColor, scale);
                    ceilColor = calculateLightPixelColor(light, ceilColor, scale);
                }
                pixels[x + y * GameSettings.VIEW_WIDTH] = floorColor;
                pixels[x + (GameSettings.VIEW_HEIGHT - y - 1) * GameSettings.VIEW_WIDTH] = ceilColor;
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

                            if (GameSettings.ENABLE_LIGHT && light != null) {
                                double scale = Mathf.clamp(light.radius / (perpWallDist > 0 ? perpWallDist : 1), 0, 1);
                                color = calculateLightPixelColor(light, color, scale);
                            }
                            pixels[x  + y * GameSettings.VIEW_WIDTH] = (int) color;
                        }

                        zBuffer[x] = perpWallDist;
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

        // SPRITE CASTING
        ArrayList<SpriteRenderer> sprites = GameObject.<SpriteRenderer>getComponents(SpriteRenderer.class);
        double posX = Camera.main.transform.position.x;
        double posY = Camera.main.transform.position.y;
        double planeX = ((CameraTransform)Camera.main.transform).xPlane;
        double planeY = ((CameraTransform)Camera.main.transform).yPlane;
        double dirX = Camera.main.transform.lookDirection.x;
        double dirY = Camera.main.transform.lookDirection.y;

        //after sorting the sprites, do the projection and draw them
        for(SpriteRenderer sr : sprites) {
            double spriteX = sr.gameObject.transform.position.x - posX;
            double spriteY = sr.gameObject.transform.position.y - posY;
            double invDet = 1.0 / (planeX * dirY - dirX * planeY); //required for correct matrix multiplication

            double transformX = invDet * (dirY * spriteX - dirX * spriteY);
            double transformY = invDet * (-planeY * spriteX + planeX * spriteY); //this is actually the depth inside the screen, that what Z is in 3D

            int spriteScreenX = (int) ((GameSettings.VIEW_WIDTH / 2) * (1 + transformX / transformY));

            int spriteHeight = (int) Mathf.abs((GameSettings.VIEW_HEIGHT / (transformY)));

            int drawStartY = -spriteHeight / 2 + GameSettings.VIEW_HEIGHT / 2;
            if (drawStartY < 0) {
                drawStartY = 0;
            }

            int drawEndY = spriteHeight / 2 + GameSettings.VIEW_HEIGHT / 2;
            if (drawEndY >= GameSettings.VIEW_HEIGHT) {
                drawEndY = GameSettings.VIEW_HEIGHT - 1;
            }

            //calculate width of the sprite
            int spriteWidth = (int) Mathf.abs((GameSettings.VIEW_HEIGHT / (transformY)));
            int drawStartX = -spriteWidth / 2 + spriteScreenX;
            if (drawStartX < 0) {
                drawStartX = 0;
            }

            int drawEndX = spriteWidth / 2 + spriteScreenX;
            if (drawEndX >= GameSettings.VIEW_WIDTH) {
                drawEndX = GameSettings.VIEW_WIDTH - 1;
            }

            Sprite spriteTexture = sr.sprites[Direction.FORWARD.getValue()];

            for (int stripe = drawStartX; stripe < drawEndX; stripe++) {
                int texX = (int) ((256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * spriteTexture.width / spriteWidth) / 256);
                if (transformY > 0 && stripe > 0 && stripe < GameSettings.VIEW_WIDTH && transformY < zBuffer[stripe]) {
                    for (int y = drawStartY; y < drawEndY; y++)
                    {
                        int d = (y) * 256 - GameSettings.VIEW_HEIGHT * 128 + spriteHeight * 128;
                        int texY = ((d * spriteTexture.height) / spriteHeight) / 256;
                        int color = spriteTexture.pixels[spriteTexture.width * texY + texX];

                        // calculate transparency compositioning
                        int newColor = calculateTransparencyComposition(pixels[stripe + y * GameSettings.VIEW_WIDTH], color);
                        pixels[stripe + y * GameSettings.VIEW_WIDTH] = newColor;
                    }
                }
            }
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

    private static int calculateTransparencyComposition(int colorBack, int colorFront) {
        double a1 = (colorBack >> 24) / 255d;
        int r1 = (colorBack >> 16) & 0x00FF;
        int g1 = (colorBack >> 8) & 0x00FF;
        int b1 = colorBack & 0x00FF;

        double a0 = (colorFront >> 24) / 255d;
        int r0 = (colorFront >> 16) & 0x00FF;
        int g0 = (colorFront >> 8) & 0x00FF;
        int b0 = colorFront & 0x00FF;

        double a01 = ((1 - a0) * a1 + a0);
        int r01 = (int) (((1 - a0) * a1 * r1 + a0 * r0) / a01);
        int g01 = (int) (((1 - a0) * a1 * g1 + a0 * g0) / a01);
        int b01 = (int) (((1 - a0) * a1 * b1 + a0 * b0) / a01);

        int rgb = (int) (a01 * 255);
        rgb = (rgb << 8) + r01;
        rgb = (rgb << 8) + g01;
        rgb = (rgb << 8) + b01;

        return rgb;
    }

}
