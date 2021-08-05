package app.assets.player;

import app.engine.core.input.Input;
import app.engine.core.components.GameObject;
import app.engine.core.math.Vector2;
import app.engine.core.math.Vector3;
import javafx.scene.input.KeyCode;

public class Player extends GameObject {

    public Player() {
        super();
        this.transform.position.set(new Vector2(100, 100));
        this.tag = "PLAYER";
        this.layer = 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (Input.getInstance().keyDown(KeyCode.W)) {
            movePlayer();
        }
    }

    public void movePlayer() {
        this.transform.position.add(new Vector3(10, 0, 0));
    }
}
