package app.engine.core.components;

import app.assets.levels.Level0;
import app.engine.core.components.GameObject;
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
    public boolean isStatic;

    public ArrayList<GameObject> children;

    public Transform() {
        this.position = new Vector3();
        this.lookDirection = new Vector2();
        this.scale = new Vector3();
        children = new ArrayList<>();

        isCollidable = true;
        isStatic = false;
    }

    public void move(double moveSpeed) {

        if (isStatic) {
            return;
        }


        if (isCollidable) {

            Level0 level = Game.getInstance().level;

            if (level.map[(int)(position.x + lookDirection.x * moveSpeed)][(int)position.y] == 0) {
                position.x += lookDirection.x * moveSpeed;
            }
            if (level.map[(int)position.x][(int)(position.y + lookDirection.y * moveSpeed)] == 0) {
                position.y += lookDirection.y * moveSpeed;
            }

        } else {
            position.x += lookDirection.x * moveSpeed;
            position.y += lookDirection.y * moveSpeed;
        }

        // recursively move children
        for (GameObject gameObject : children) {
            gameObject.transform.isCollidable = isCollidable;
            gameObject.transform.move(moveSpeed);
        }
    }

    public void rotate(double rotSpeed) {

        if (isStatic) {
            return;
        }

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
