package app.engine.core.exception;

public class NoLevelException extends Exception {
    public NoLevelException() {
        super("Game has no level.");
    }
}
