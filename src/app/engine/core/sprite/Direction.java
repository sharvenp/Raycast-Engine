package app.engine.core.sprite;

public enum Direction {

    FORWARD(0),
    RIGHT(1),
    LEFT(2),
    BACK(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
