package app.assets.gameobjects;

import app.engine.core.components.GameObject;
import app.engine.core.math.Vector2;

public class Enemy extends GameObject {

    public Enemy() throws Exception {
        tag = "ENEMY";
        transform.position = new Vector2(5, 4);
        transform.lookDirection = new Vector2(-1, 0);
    }

}
