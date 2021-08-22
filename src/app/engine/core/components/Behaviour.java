package app.engine.core.components;

public abstract class Behaviour extends Component {

    public abstract void start() throws Exception;

    public abstract void update();

}
