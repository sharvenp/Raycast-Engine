package app.engine.core.game;

import app.engine.core.components.GameObject;
import app.engine.core.exception.NoCameraException;
import app.engine.core.renderer.Camera;
import javafx.animation.AnimationTimer;

public class Game extends AnimationTimer {

    private static Game instance;
    private static Camera camera;

    public static double timeNow;
    public static double deltaTime;

    private Game(){

    }

    public synchronized static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    public void setCamera(Camera newCamera) {
        camera = newCamera;
    }

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
        deltaTime = ((currentNanoTime - timeNow) / 1e9);
        timeNow = currentNanoTime;

        // update all game objects
        for (GameObject gameObject : GameObject.gameObjects) {
            gameObject.update();
        }

        // update renderer
        camera.update();
    }
}
