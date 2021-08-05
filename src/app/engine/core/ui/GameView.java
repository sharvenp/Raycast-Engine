package app.engine.core.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameView extends Pane {

    final Canvas canvas;

    public GameView(double width, double height) {
        setWidth(width);
        setHeight(height);
        canvas = new Canvas(width, height);
        getChildren().add(canvas);
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
}