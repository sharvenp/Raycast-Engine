package app.engine.core.components;

public abstract class Behaviour {

    public GameObject gameObject;

    public abstract void start() throws Exception;

    public abstract void update();

}
