package app.assets.player;

import app.assets.camera.RaycastCamera;
import app.assets.levels.Level;
import app.engine.core.exception.NoCameraException;
import app.engine.core.game.Game;
import app.engine.core.input.Input;
import app.engine.core.components.GameObject;
import app.engine.core.math.Mathf;
import app.engine.core.math.Vector2;
import app.engine.core.renderer.Camera;
import javafx.scene.input.KeyCode;

public class Player extends GameObject {

    public RaycastCamera camera;

    private double moveSpeed = 0.08;
    private double rotSpeed = 0.045d;

    public Player() {
        super();

        transform.position.set(new Vector2(4.5, 4.5));
        transform.lookDirection.set(new Vector2(1, 0));

        tag = "PLAYER";
        layer = 0;
    }

    @Override
    public void start() throws Exception {
        camera = (RaycastCamera) Camera.main;
        if (camera == null) {
            throw new NoCameraException();
        }
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

        Level map = Game.getInstance().map;

        if (map.map[(int) (transform.position.x + transform.lookDirection.x * moveSpeed * moveDir)][(int) transform.position.y] == 0) {
            transform.position.x += transform.lookDirection.x * moveSpeed * moveDir;
        }
        if (map.map[(int) transform.position.x][(int) (transform.position.y + transform.lookDirection.y * moveSpeed * moveDir)] == 0) {
            transform.position.y += transform.lookDirection.y * moveSpeed * moveDir;
        }
    }

    public void rotate(int rotateDir) {
        if (rotateDir == 0) {
            return;
        }

        // calculate look direction
        double oldXDir = transform.lookDirection.x;
        transform.lookDirection.x = transform.lookDirection.x * Mathf.cos(rotSpeed * rotateDir) - transform.lookDirection.y * Mathf.sin(rotSpeed * rotateDir);
        transform.lookDirection.y = oldXDir * Math.sin(rotSpeed * rotateDir) + transform.lookDirection.y * Math.cos(rotSpeed * rotateDir);

        // calculate camera plane
        double oldXPlane = camera.xPlane;
        camera.xPlane = camera.xPlane * Math.cos(rotSpeed * rotateDir) - camera.yPlane * Math.sin(rotSpeed * rotateDir);
        camera.yPlane = oldXPlane * Math.sin(rotSpeed * rotateDir) + camera.yPlane * Math.cos(rotSpeed * rotateDir);
    }
}
