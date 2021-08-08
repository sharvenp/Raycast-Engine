package app.engine.core.components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class GameObject {

    public static ArrayList<GameObject> hierarchy;

    public Transform transform = new Transform();
    public String tag = "";

    public abstract void start() throws Exception;

    public abstract void update();

    public static GameObject findObjectWithTag(String tag) {
        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            if (gameObject.tag.equals(tag)) {
                return gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

    public GameObject findChildObjectWithTag(String tag) {
        Queue<GameObject> queue = new LinkedList<GameObject>(transform.children);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            if (gameObject.tag.equals(tag)) {
                return gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

}
