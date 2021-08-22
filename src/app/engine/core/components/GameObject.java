package app.engine.core.components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class GameObject {

    public static ArrayList<GameObject> hierarchy;

    public Transform transform = new Transform();
    public String tag = "";
    public ArrayList<Component> components = new ArrayList<>();

    public void addComponent(Component ...components) {
        for (Component component : components) {
            component.gameObject = this;
            this.components.add(component);
        }
    }

    public static <T extends GameObject> T findObjectWithTag(String tag) {
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

    public static <T extends GameObject> ArrayList<T> findObjectsWithTag(String tag) {

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

    public static <T extends Component> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<T> components = new ArrayList<>();

        Queue<GameObject> queue = new LinkedList<GameObject>(hierarchy);
        while (!queue.isEmpty())
        {
            GameObject gameObject = (GameObject) queue.poll();
            Component component = gameObject.<T>getComponent(type);
            if (component != null) {
                components.add((T) component);
            }
            queue.addAll(gameObject.transform.children);
        }

        return components;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component component : components) {
            if (component.getClass().toString().equals(type.toString())) {
                return type.cast(component);
            }
        }
        return null;
    }

}
