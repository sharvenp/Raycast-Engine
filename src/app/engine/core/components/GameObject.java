package app.engine.core.components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class GameObject {

    public static ArrayList<GameObject> hierarchy;

    public Transform transform = new Transform();
    public String tag = "";
    public ArrayList<Behaviour> behaviours = new ArrayList<>();

    public void addBehaviours(Behaviour ...behaviours) {
        for (Behaviour behaviour : behaviours) {
            behaviour.gameObject = this;
            this.behaviours.add(behaviour);
        }
    }

    public static <T extends GameObject> GameObject findObjectWithType(String tag) {
        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = queue.poll();
            if (gameObject.tag.equals(tag)) {
                return (T) gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

    public static <T extends GameObject> ArrayList<T> findObjectsWithType(String tag) {

        ArrayList<T> objects = new ArrayList<>();

        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = (GameObject) queue.poll();
            if (gameObject.tag.equals(tag)) {
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
            if (gameObject.tag.equals(tag)) {
                return (T) gameObject;
            }
            queue.addAll(gameObject.transform.children);
        }

        return null;
    }

    public <T extends GameObject> ArrayList<T> findChildObjectsWithType(String tag) {

        ArrayList<T> objects = new ArrayList<>();

        Queue<GameObject> queue = new LinkedList<GameObject>(transform.children);
        while (!queue.isEmpty())
        {
            GameObject gameObject = (GameObject) queue.poll();
            if (gameObject.tag.equals(tag)) {
                objects.add((T) gameObject);
            }
            queue.addAll(gameObject.transform.children);
        }

        return objects;
    }

    public Behaviour findBehaviour(String classType) {
        for (Behaviour behaviour : behaviours) {
            if (behaviour.getClass().toString().equals(classType)) {
                return behaviour;
            }
        }
        return null;
    }

}
