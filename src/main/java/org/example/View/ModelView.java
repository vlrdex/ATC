package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Model.ACModel;

public class ModelView {
    private Stage stage;
    private ACModel model;

    public ModelView(Stage stage, ACModel model) {
        this.stage = stage;
        this.model = model;

        Text modelText = new Text(model.toString());
        modelText.setFill(javafx.scene.paint.Color.WHITE);
        modelText.setFont(Font.font("Consolas", 16));

        VBox root = new VBox(modelText);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        root.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.setTitle("Aircraft Model Details");

        // Set minimum size and show
        stage.setMinWidth(200);
        stage.setMinHeight(300);
        stage.show();
    }

    public ModelView(ACModel model) {
        this(new Stage(), model);
    }
}
