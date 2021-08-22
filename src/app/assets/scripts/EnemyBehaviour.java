package app.assets.scripts;

import app.engine.core.components.Behaviour;
import app.engine.core.components.SpriteRenderer;

public class EnemyBehaviour extends Behaviour {

    private SpriteRenderer sr;

    @Override
    public void start() throws Exception {
        sr = gameObject.<SpriteRenderer>getComponent(SpriteRenderer.class);
    }

    @Override
    public void update() {
    }
}
