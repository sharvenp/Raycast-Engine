package app.assets.gameobjects;

import app.engine.core.components.GameObject;
import app.engine.core.math.Vector2;

public class Player extends GameObject {
    public Player() {
        tag = "PLAYER";
        transform.position.set(new Vector2(4.5, 4.5));
        transform.lookDirection.set(new Vector2(1, 0));
    }
}
