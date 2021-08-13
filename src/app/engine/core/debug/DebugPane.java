package app.engine.core.debug;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class DebugPane extends BorderPane {

    public static Label fpsLabel;
    public static TextFlow debugTextArea;
    public static Stage debugStage;

    public DebugPane() {
        super();
        fpsLabel = new Label("FPS: ");
        debugTextArea = new TextFlow();
        this.setTop(fpsLabel);
        this.setCenter(debugTextArea);
    }
}
