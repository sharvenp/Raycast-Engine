package app.engine.core.renderer;

import javafx.scene.layout.BorderPane;
import javafx.scene.image.ImageView;

public class RenderView extends BorderPane {

    public final ImageView renderView;

    public RenderView(double width, double height) {
        super();
        setWidth(width);
        setHeight(height);
        renderView = new ImageView();
        renderView.fitWidthProperty().bind(this.widthProperty());
        renderView.fitHeightProperty().bind(this.heightProperty());
        getChildren().add(renderView);
    }

    public ImageView getRenderView() {
        return this.renderView;
    }
}