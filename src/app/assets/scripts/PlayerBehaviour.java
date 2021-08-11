package app.assets.scripts;

import app.engine.core.components.Behaviour;
import app.engine.core.input.Input;
import javafx.scene.input.KeyCode;

public class PlayerBehaviour extends Behaviour {

    private final double moveSpeed = 0.1;
    private final double rotSpeed = 0.045d;

    @Override
    public void start() throws Exception {


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
        gameObject.transform.move(moveSpeed * moveDir);
    }

    public void rotate(int rotateDir) {
        if (rotateDir == 0) {
            return;
        }
        gameObject.transform.rotate(rotSpeed * rotateDir);
    }
}
