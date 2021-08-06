package app.assets;

import app.assets.camera.RaycastCamera;
import app.assets.levels.Level;
import app.assets.player.Player;
import app.engine.core.components.GameObject;
import app.engine.core.game.Game;
import app.engine.core.input.Input;
import app.engine.core.renderer.Camera;
import app.engine.core.ui.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameView rootPane = new GameView(1280, 720);

        primaryStage.setTitle("Some Game");
        primaryStage.setScene(new Scene(rootPane));
        primaryStage.setResizable(false);

        Level map = new Level();

        Camera.main = new RaycastCamera(primaryStage.getScene(), rootPane.getCanvas());
        Game.getInstance().setMap(map);
        Game.getInstance().setCamera(Camera.main);
        Input.getInstance().pollScene(primaryStage.getScene());

        // Add game objects and maps here
        Player player = new Player();
        GameObject.gameObjects.add(player);

        Game.getInstance().initialize();
        Game.getInstance().start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
