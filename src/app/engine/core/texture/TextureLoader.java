package app.engine.core.texture;

import app.assets.Launcher;
import app.engine.core.debug.Debug;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class TextureLoader {

    private static ArrayList<Texture> textures;

    public static ArrayList<Texture> getTextures()  {
        return textures;
    }

    public static void loadTextures() throws Exception {

        System.out.println();

        URI textureDirPath = Launcher.class.getResource("textures/").toURI();
        textures = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(textureDirPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            BufferedImage image = ImageIO.read(path.toFile());
                            int w = image.getWidth();
                            int h = image.getHeight();
                            if (w != h) {
                                Debug.error("Texture is not square:", path);
                            } else {
                                Texture texture = new Texture(path.toString(), w);
                                image.getRGB(0, 0, w, h, texture.pixels, 0, w);
                                Debug.log("Loaded texture:", path);
                                textures.add(texture);
                            }
                        } catch (Exception e) {
                            Debug.error("Error loading texture: ", path, "( " + e + " ) - skipping...");
                        }
                    });
        }
    }

}
