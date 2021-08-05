package app.main;

import app.engine.input.Input;
import app.engine.game.Game;
import app.engine.renderer.Renderer;
import app.ui.CanvasPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        CanvasPane rootPane = new CanvasPane(1280, 720);
        primaryStage.setTitle("Some Game");
        primaryStage.setScene(new Scene(rootPane));
        primaryStage.setResizable(false);

        Renderer renderer = new Renderer(primaryStage.getScene(), rootPane.getCanvas());
        Game game = new Game(renderer);

        Input.getInstance().pollScene(primaryStage.getScene());

        game.start();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
