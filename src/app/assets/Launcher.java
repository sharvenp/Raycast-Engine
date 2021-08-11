package app.assets;

import app.engine.core.components.Light;
import app.engine.core.math.Vector2;
import app.engine.core.renderer.RaycastRenderer;
import app.assets.levels.Level0;
import app.assets.player.Player;
import app.engine.core.components.GameObject;
import app.engine.core.game.Game;
import app.engine.core.input.Input;
import app.engine.core.components.Camera;
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
        Camera.main.light = new Light(new Color(50, 50, 50), 0.5, 1, 0);
        Camera.main.light.radius = 1;
        Camera.main.light.intensity = 1;

        // set up engine stuff
        RaycastRenderer.initialize(primaryStage.getScene(), rootPane.getRenderView());
        Game.getInstance().setCamera(Camera.main);
        Input.getInstance().pollScene(primaryStage.getScene());

        // add level
        Level0 map = new Level0();
        Game.getInstance().setLevel(map);

        // create hierarchy
        // for now, manually create it

        Player player = new Player();
        player.transform.children.add(Camera.main);

        GameObject.hierarchy = new ArrayList<>();
        GameObject.hierarchy.addAll(Arrays.asList(player));

        // load resources
        TextureLoader.loadTextures();

        Game.getInstance().initialize();
        Game.getInstance().start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
