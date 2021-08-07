package app.engine.core.game;

import app.assets.levels.Level;
import app.engine.core.components.GameObject;
import app.engine.core.exception.NoCameraException;
import app.engine.core.renderer.Camera;
import javafx.animation.AnimationTimer;

public class Game extends AnimationTimer {

    private static Game instance;
    public Camera camera;
    public Level map;

    public synchronized static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setMap(Level map) { this.map = map; }

    public void initialize() throws Exception {
        if (camera == null) {
            throw new NoCameraException();
        }

        camera.initializeScreen();

        for (GameObject gameObject : GameObject.gameObjects) {
            gameObject.start();
        }
    }

    @Override
    public void handle(long currentNanoTime) {
        Time.deltaTime = currentNanoTime - Time.now;
        Time.now = currentNanoTime / 1e9;

        // update all game objects
        for (GameObject gameObject : GameObject.gameObjects) {
            gameObject.update();
        }

        // update renderer
        camera.update();
    }
}
