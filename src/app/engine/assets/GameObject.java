package app.engine.assets;

public abstract class GameObject {

    public Transform transform;
    public String tag;

    public GameObject() {
        transform = new Transform();
    }

    public abstract void start();

    public abstract void update();
}
