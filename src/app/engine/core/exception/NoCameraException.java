package app.engine.core.exception;

public class NoCameraException extends Exception {
    public NoCameraException() {
        super("Game has no camera.");
    }
}
