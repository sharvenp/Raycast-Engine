package app.engine.core.components;

import app.engine.core.game.Game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class GameObject {

    public static ArrayList<GameObject> hierarchy;

    public Transform transform = new Transform();
    public String type = "";

    public abstract void start() throws Exception;

    public abstract void update();

    public static <T extends GameObject> GameObject findObjectWithType(String type) {
        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            if (gameObject.type.equals(type)) {
                return (T) gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

    public static <T extends GameObject> ArrayList<T> findObjectsWithType(String type) {

        ArrayList<T> objects = new ArrayList<>();

        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = (GameObject) queue.poll();
            if (gameObject.type.equals(type)) {
                objects.add((T) gameObject);
            }
            queue.addAll(gameObject.transform.children);
        }

        return objects;
    }

    public <T extends GameObject> GameObject findChildObjectWithTag(String tag) {
        Queue<GameObject> queue = new LinkedList<GameObject>(transform.children);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            if (gameObject.type.equals(tag)) {
                return (T) gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

    public <T extends GameObject> ArrayList<T> findChildObjectsWithType(String type) {

        ArrayList<T> objects = new ArrayList<>();

        Queue<GameObject> queue = new LinkedList<GameObject>(transform.children);
        while (!queue.isEmpty())
        {
            GameObject gameObject = (GameObject) queue.poll();
            if (gameObject.type.equals(type)) {
                objects.add((T) gameObject);
            }
            queue.addAll(gameObject.transform.children);
        }

        return objects;
    }

}
