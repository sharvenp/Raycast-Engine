package app.engine.core.sprite;

import app.assets.Launcher;
import app.engine.core.debug.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SpriteLoader {

    public static Sprite loadSprite(String fileName) throws Exception {

        URI spriteDirPath = Launcher.class.getResource("sprites/"+fileName).toURI();
        Path path = Paths.get(spriteDirPath);
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            int w = image.getWidth();
            int h = image.getHeight();
            Sprite sprite = new Sprite(w, h);
            image.getRGB(0, 0, w, h, sprite.pixels, 0, w);
            Debug.log("Loaded sprite:", path);

            return sprite;

        } catch (Exception e) {
            Debug.error("Error loading sprite: ", path, "( " + e + " ) - skipping...");
        }
        return null;
    }

}
