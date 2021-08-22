package app.assets;

import app.assets.gameobjects.Enemy;
import app.assets.scripts.EnemyBehaviour;
import app.assets.scripts.PlayerMovement;
import app.engine.core.components.Light;
import app.engine.core.debug.DebugPane;
import app.engine.core.math.Vector2;
import app.engine.core.renderer.RaycastRenderer;
import app.assets.levels.Level0;
import app.assets.gameobjects.Player;
import app.engine.core.components.GameObject;
import app.engine.core.game.Game;
import app.engine.core.input.Input;
import app.engine.core.components.Camera;
import app.engine.core.sprite.Direction;
import app.engine.core.components.SpriteRenderer;
import app.engine.core.sprite.Sprite;
import app.engine.core.texture.TextureLoader;
import app.engine.core.renderer.RenderView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameSettings.initialize();

        RenderView rootPane = new RenderView(GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT);

        primaryStage.setTitle("Some Game");
        primaryStage.setScene(new Scene(rootPane));

        // Create camera
        Camera.main = new Camera();
        Camera.main.addComponent(new Light(new Color(53, 44, 20), 1, 1));

        // set up engine stuff
        RaycastRenderer.initialize(primaryStage.getScene(), rootPane.getRenderView());
        Game.getInstance().setCamera(Camera.main);
        Input.getInstance().pollScene(primaryStage.getScene());

        // add level
        Level0 map = new Level0();
        Game.getInstance().setLevel(map);

        // create hierarchy

        Player player = new Player();
        player.addComponent(new PlayerMovement());
        Camera.main.transform.position.set(new Vector2().set(player.transform.position));
        Camera.main.transform.lookDirection.set(new Vector2().set(player.transform.lookDirection));
        player.transform.children.add(Camera.main);

        Enemy enemy = new Enemy();
        SpriteRenderer sr = new SpriteRenderer();
        sr.setSprite("man.png", Direction.FORWARD);
        sr.zIndex = 1;
        enemy.addComponent(sr);
        enemy.addComponent(new EnemyBehaviour());

        GameObject.hierarchy = new ArrayList<>();
        GameObject.hierarchy.addAll(Arrays.asList(player, enemy));

        // load resources
        TextureLoader.loadTextures();

        Game.getInstance().initialize();
        Game.getInstance().start();
        primaryStage.show();

        if (GameSettings.DEBUG_MODE) {
            primaryStage.setOnCloseRequest(windowEvent -> {
                DebugPane.debugStage.close();
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
