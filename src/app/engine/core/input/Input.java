package app.engine.core.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Input {

    private static Scene scene;
    private static Input instance;
    private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();

    private Input() {
    }

    public synchronized static Input getInstance() {
        if (instance == null) {
            instance = new Input();
        }
        return instance;
    }

    public void pollScene(Scene scene) {
        clearKeys();
        removeCurrentKeyHandlers();
        setScene(scene);
    }

    private void clearKeys() {
        keysCurrentlyDown.clear();
    }

    private void removeCurrentKeyHandlers() {
        if (scene != null) {
            Input.scene.setOnKeyPressed(null);
            Input.scene.setOnKeyReleased(null);
        }
    }

    private void setScene(Scene scene) {
        Input.scene = scene;
        Input.scene.setOnKeyPressed((keyEvent -> {
            keysCurrentlyDown.add(keyEvent.getCode());
        }));
        Input.scene.setOnKeyReleased((keyEvent -> {
            keysCurrentlyDown.remove(keyEvent.getCode());
        }));
    }

    public boolean keyDown(KeyCode keyCode) {
        return keysCurrentlyDown.contains(keyCode);
    }
}
