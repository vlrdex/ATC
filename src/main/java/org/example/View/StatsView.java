package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Model.Stats;

public class StatsView {

    private Stage stage;
    private Stats stats;

    public StatsView(Stage stage, Stats stats) {
        this.stage = stage;
        this.stats = stats;

        Text statsText = new Text(stats.toString());
        statsText.setFill(Color.WHITE);
        statsText.setFont(Font.font("Consolas", 16));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());

        VBox root = new VBox(statsText, closeButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setSpacing(15);
        root.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.setTitle("Stats Details");

        stage.setMinWidth(200);
        stage.setMinHeight(300);
        stage.show();
    }

    public StatsView(Stats stats) {
        this(new Stage(), stats);
    }
}