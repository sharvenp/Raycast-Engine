package app.engine.core.components;

import app.engine.core.sprite.Direction;
import app.engine.core.sprite.Sprite;
import app.engine.core.sprite.SpriteLoader;

public class SpriteRenderer extends Component{

    public Sprite[] sprites = new Sprite[4];
    public Direction currentDirection = Direction.FORWARD;
    public int zIndex;

    public void setSprite(String name, Direction direction) throws Exception {
        this.sprites[direction.getValue()] = SpriteLoader.loadSprite(name);
    }
}
