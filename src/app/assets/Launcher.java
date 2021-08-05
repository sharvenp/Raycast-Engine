package app.assets;

import app.assets.camera.RaycastCamera;
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

        Camera camera = new RaycastCamera(primaryStage.getScene(), rootPane.getCanvas());
        Game.getInstance().setCamera(camera);
        Input.getInstance().pollScene(primaryStage.getScene());

        initialize();
        Game.getInstance().initialize();

        Game.getInstance().start();
        primaryStage.show();
    }

    public void initialize() {
        // Add game objects and maps here
        GameObject.gameObjects.add(new Player());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
