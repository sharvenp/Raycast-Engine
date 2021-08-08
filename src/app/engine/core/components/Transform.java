package app.engine.core.components;

import app.assets.levels.Level;
import app.engine.core.debug.Debug;
import app.engine.core.game.Game;
import app.engine.core.math.Mathf;
import app.engine.core.math.Vector2;
import app.engine.core.math.Vector3;

import java.util.ArrayList;

public class Transform {

    public Vector3 position;
    public Vector2 lookDirection;
    public Vector3 scale;
    public boolean isCollidable;

    public ArrayList<GameObject> children;

    public Transform() {
        this.position = new Vector3();
        this.lookDirection = new Vector2();
        this.scale = new Vector3();
        children = new ArrayList<>();

        isCollidable = true;
    }

    public void move(double moveSpeed) {

        boolean positionChanged = false;

        if (isCollidable) {

            Level level = Game.getInstance().level;

            if (level.map[(int)(position.x + lookDirection.x * moveSpeed)][(int)position.y] == 0) {
                position.x += lookDirection.x * moveSpeed;
                positionChanged = true;
            }
            if (level.map[(int)position.x][(int)(position.y + lookDirection.y * moveSpeed)] == 0) {
                position.y += lookDirection.y * moveSpeed;
                positionChanged = true;
            }

        } else {
            position.x += lookDirection.x * moveSpeed;
            position.y += lookDirection.y * moveSpeed;
            positionChanged = true;
        }

        if (positionChanged) {
            // recursively move children
            for (GameObject gameObject : children) {
                gameObject.transform.isCollidable = isCollidable;
                gameObject.transform.move(moveSpeed);
            }
        }
    }

    public void rotate(double rotSpeed) {
        // calculate look direction
        double oldXDir = lookDirection.x;
        lookDirection.x = lookDirection.x * Mathf.cos(rotSpeed) - lookDirection.y * Mathf.sin(rotSpeed);
        lookDirection.y = oldXDir * Math.sin(rotSpeed) + lookDirection.y * Math.cos(rotSpeed);

        // recursively rotate children
        for (GameObject gameObject : children) {
            gameObject.transform.rotate(rotSpeed);
        }
    }
}
