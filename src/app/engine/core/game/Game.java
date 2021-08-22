package app.engine.core.game;

import app.assets.GameSettings;
import app.assets.levels.Level0;
import app.engine.core.components.Behaviour;
import app.engine.core.components.Component;
import app.engine.core.components.GameObject;
import app.engine.core.debug.DebugPane;
import app.engine.core.exception.NoCameraException;
import app.engine.core.exception.NoLevelException;
import app.engine.core.components.Camera;
import app.engine.core.renderer.RaycastRenderer;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Queue;

public class Game extends AnimationTimer {

    private static Game instance;
    public Camera camera;
    public Level0 level;

    public synchronized static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setLevel(Level0 level) { this.level = level; }

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
            for (Component behaviour : gameObject.components) {
                if (behaviour instanceof Behaviour) {
                    ((Behaviour)behaviour).start();
                }
            }
            queue.addAll(gameObject.transform.children);
        }
    }

    @Override
    public void handle(long currentNanoTime) {
        double currentMSTime = currentNanoTime / 1e9;
        Time.deltaTime = currentMSTime - Time.now;
        Time.now = currentMSTime;

        if (GameSettings.DEBUG_MODE) {
            double fps = 1.0 / Time.deltaTime;
            DebugPane.fpsLabel.setText(String.format("FPS: %.2f MS: %.2fms", fps, Time.deltaTime * 1000));
            if (fps < 60) {
                DebugPane.fpsLabel.setTextFill(Color.RED);
            } else {
                DebugPane.fpsLabel.setTextFill(Color.GREEN);
            }
        }

        Queue<GameObject> queue = new LinkedList<GameObject>(GameObject.hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            for (Component behaviour : gameObject.components) {
                if (behaviour instanceof Behaviour) {
                    ((Behaviour)behaviour).update();
                }
            }
            queue.addAll(gameObject.transform.children);
        }

        RaycastRenderer.render();

    }
}
