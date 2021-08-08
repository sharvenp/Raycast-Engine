package app.engine.core.game;

import app.assets.levels.Level;
import app.engine.core.components.GameObject;
import app.engine.core.exception.NoCameraException;
import app.engine.core.exception.NoLevelException;
import app.engine.core.renderer.camera.Camera;
import app.engine.core.renderer.RaycastRenderer;
import javafx.animation.AnimationTimer;

import java.util.LinkedList;
import java.util.Queue;

public class Game extends AnimationTimer {

    private static Game instance;
    public Camera camera;
    public Level level;

    public synchronized static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setLevel(Level level) { this.level = level; }

    public void initialize() throws Exception {
        if (camera == null) {
            throw new NoCameraException();
        }

        if (level == null) {
            throw new NoLevelException();
        }

        Queue<GameObject> queue = new LinkedList<GameObject>(GameObject.hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            gameObject.start();
            queue.addAll(gameObject.transform.children);
        }
    }

    @Override
    public void handle(long currentNanoTime) {
        Time.deltaTime = currentNanoTime - Time.now;
        Time.now = currentNanoTime / 1e9;

        Queue<GameObject> queue = new LinkedList<GameObject>(GameObject.hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            gameObject.update();
            queue.addAll(gameObject.transform.children);
        }

        RaycastRenderer.render();
    }
}
