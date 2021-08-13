package app.engine.core.renderer;

import app.assets.GameSettings;
import app.engine.core.debug.DebugPane;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RenderView extends StackPane {

    public final ImageView renderView;

    public Label fpsLabel;
    public TextArea debugTextArea;

    public RenderView(double width, double height) {
        super();
        setWidth(width);
        setHeight(height);
        renderView = new ImageView();
        renderView.fitWidthProperty().bind(this.widthProperty());
        renderView.fitHeightProperty().bind(this.heightProperty());

        this.getChildren().add(renderView);

        if (GameSettings.DEBUG_MODE) {
            Stage stage = new Stage();
            stage.setTitle("VectorEngine Debug Window");
            stage.setScene(new Scene(new DebugPane(), 1000, 500));
            stage.sizeToScene();
            DebugPane.debugStage = stage;
            stage.show();
        }

        renderView.requestFocus();
    }

    public ImageView getRenderView() {
        return this.renderView;
    }
    public Label getFPSLabel() {
        return this.fpsLabel;
    }
    public TextArea getDebugTextArea() {
        return this.debugTextArea;
    }
}