package app.engine.core.components;

import app.engine.core.exception.NoCameraException;

import java.util.ArrayList;

public abstract class GameObject {

    public static ArrayList<GameObject> gameObjects;

    static {
        gameObjects = new ArrayList<>();
    }

    public static GameObject findObjectWithTag(String tag) {
        GameObject obj = null;
        for (GameObject gameObject : gameObjects) {
            if (gameObject.tag.equals(tag)) {
                obj = gameObject;
            }
        }
        return obj;
    }

    public Transform transform;
    public String tag;
    public int layer;

    public GameObject() {
        this.tag = "";
        this.layer = -1;
        this.transform = new Transform();
    }

    public abstract void start() throws Exception;

    public abstract void update();
}
