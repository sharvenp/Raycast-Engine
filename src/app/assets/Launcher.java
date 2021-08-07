package app.assets;

import app.engine.core.renderer.RaycastCamera;
import app.assets.levels.Level;
import app.assets.player.Player;
import app.engine.core.components.GameObject;
import app.engine.core.game.Game;
import app.engine.core.input.Input;
import app.engine.core.renderer.Camera;
import app.engine.core.texture.TextureLoader;
import app.engine.core.renderer.RenderView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameSettings.initialize();

        RenderView rootPane = new RenderView(GameSettings.VIEW_WIDTH, GameSettings.VIEW_HEIGHT);

        primaryStage.setTitle("Some Game");
        primaryStage.setScene(new Scene(rootPane));

        // set up engine stuff
        Camera.main = new RaycastCamera(primaryStage.getScene(), rootPane.getRenderView());
        Game.getInstance().setCamera(Camera.main);
        Input.getInstance().pollScene(primaryStage.getScene());

        // load resources
        TextureLoader.loadTextures();

        // add game objects
        Player player = new Player();
        GameObject.gameObjects.add(player);

        // add level
        Level map = new Level();
        Game.getInstance().setMap(map);

        Game.getInstance().initialize();
        Game.getInstance().start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
