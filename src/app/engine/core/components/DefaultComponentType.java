package app.engine.core.components;

public enum DefaultComponentType {

    CAMERA("CAMERA"),
    LIGHT("LIGHT");

    private final String type;

    DefaultComponentType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
