package app.engine.game;

import app.engine.assets.GameObject;
import app.engine.assets.player.Player;
import app.engine.renderer.Renderer;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class Game extends AnimationTimer {

    private final Renderer renderer;
    private ArrayList<GameObject> gameObjects;

    public static double timeNow;
    public static double deltaTime;

    public Game(Renderer renderer) {

        this.renderer = renderer;

        gameObjects = new ArrayList<GameObject>();

        gameObjects.add(new Player());

        renderer.initScreen();
        for (GameObject gameObject : this.gameObjects) {
            gameObject.start();
        }
    }

    public GameObject findObjectWithTag(String tag) {
        GameObject obj = null;
        for (GameObject gameObject : this.gameObjects) {
            if (gameObject.tag.equals(tag)) {
                obj = gameObject;
            }
        }
        return obj;
    }

    @Override
    public void handle(long currentNanoTime) {
        deltaTime = ((currentNanoTime - timeNow) / 1e9);
        timeNow = currentNanoTime;

        // update all game objects
        for (GameObject gameObject : this.gameObjects) {
            gameObject.update();
        }

        // update renderer
        renderer.render(this);
    }
}
