package app.assets.player;

import app.engine.core.components.Light;
import app.engine.core.input.Input;
import app.engine.core.components.GameObject;
import app.engine.core.math.Vector2;
import app.engine.core.renderer.camera.Camera;
import javafx.scene.input.KeyCode;

import java.awt.*;


public class Player extends GameObject {

    public Light light;

    private final double moveSpeed = 0.1;
    private final double rotSpeed = 0.045d;

    @Override
    public void start() throws Exception {

        tag = "PLAYER";

        transform.position.set(new Vector2(4.5, 4.5));
        transform.lookDirection.set(new Vector2(1, 0));

        Camera.main.transform.position.set(new Vector2(4.5, 4.5));
        Camera.main.transform.lookDirection.set(new Vector2(1, 0));

        light = new Light(Color.WHITE, 5, 2);
    }

    @Override
    public void update() {

        int dm = 0;
        int dr = 0;

        if (Input.getInstance().keyDown(KeyCode.W)) { dm += 1; }
        if (Input.getInstance().keyDown(KeyCode.S)) { dm -= 1; }

        if (Input.getInstance().keyDown(KeyCode.D)) { dr -= 1; }
        if (Input.getInstance().keyDown(KeyCode.A)) { dr += 1; }

        rotate(dr);
        move(dm);
    }

    public void move(int moveDir) {
        if (moveDir == 0) {
            return;
        }
        transform.move(moveSpeed * moveDir);
    }

    public void rotate(int rotateDir) {
        if (rotateDir == 0) {
            return;
        }
        transform.rotate(rotSpeed * rotateDir);
    }
}
