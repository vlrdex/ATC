package org.example.View;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.Controller.GameController;
import org.example.Model.AirPort;
import org.example.Model.Flight;
import org.example.Model.Point;
import org.example.Model.Runway;
import org.example.Model.Utils.VectorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView{

    private GameController gameController;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;
    private Stage modelStage;
    private List<String> pointNames=new ArrayList<>();


    //for terminal
    private TextArea terminalHistory;
    private TextField commandInput;


    // Control panel
    private Flight selected=null;
    private VBox controlPanel;
    private VBox aircraftListContainer;
    private VBox inputBox;
    private final Map<Flight, VBox> flightCards = new HashMap<>();
    private TextField altitudeField, speedField, degField;
    private ComboBox<String> headingInput;
    private Button applyButton;
    private Button modelButton;





    public GameView(Stage stage){
        this.gameController=GameController.getInstance(this);
        this.stage=stage;

        pointNames=new ArrayList<>(
                gameController.airPort.getNearbyPoints().stream().map(Point::getName).toList()
        );
        for (Runway runway:gameController.airPort.getRunways()){
            pointNames.addAll(runway.getEndPoints().stream().map(Point::getName).toList());
        }

        stage.setScene(init());

        updateAircraftList();

        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };

        timer.start();

        stage.getScene().getStylesheets().add(
                getClass().getResource("/comboBox.css").toExternalForm()
        );
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.show();
    }

    public GameView(){
        new GameView(new Stage());
    }

    private Scene init(){

        //Terminal and canvas

        canvas=new Canvas(1280,720);
        gc=canvas.getGraphicsContext2D();


        terminalHistory = new TextArea();
        terminalHistory.setEditable(false);
        terminalHistory.setPrefHeight(100);
        terminalHistory.setWrapText(true);
        terminalHistory.setStyle(
                "-fx-control-inner-background: black;" +
                        "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-highlight-fill: gray;" +
                        "-fx-highlight-text-fill: black;"
        );


        ScrollPane scrollPane = new ScrollPane(terminalHistory);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        commandInput = new TextField();
        commandInput.setPromptText("Enter command...");
        commandInput.setOnAction(e -> handelApply(commandInput.getText()));
        commandInput.setStyle(
                "-fx-control-inner-background: black;" +
                        "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: gray;"
        );

        var terminalAndCanvas= new VBox(canvas,scrollPane,commandInput);

        //Control panel
        controlPanel = new VBox(15);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setPrefWidth(280);
        controlPanel.setStyle("-fx-background-color: #1e1e1e;");

        Label controlTitle = new Label("Flight Control");
        controlTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
        controlTitle.setAlignment(Pos.CENTER);


        aircraftListContainer = new VBox(10);
        aircraftListContainer.setStyle("-fx-background-color: #2a2a2a; -fx-background-radius: 8;");
        aircraftListContainer.setPadding(new Insets(10));
        aircraftListContainer.setMouseTransparent(false);
        aircraftListContainer.setPickOnBounds(true);


        ScrollPane aircraftScroll = new ScrollPane(aircraftListContainer);
        aircraftScroll.setFitToWidth(true);
        aircraftScroll.setPrefHeight(600);
        aircraftScroll.setStyle("-fx-background: #2a2a2a;");
        aircraftScroll.setPickOnBounds(false);


        //Input fields
        altitudeField = createStyledTextField("Altitude");
        speedField = createStyledTextField("Speed");
        degField = createStyledTextField("Deg");


        ObservableList<String> options = FXCollections.observableArrayList(pointNames);
        FilteredList<String> filteredOptions = new FilteredList<>(options, s -> true);

        headingInput = new ComboBox<>(filteredOptions);
        headingInput.setEditable(true);

            headingInput.setStyle("""
                -fx-background-color: #1b1b1b;
                -fx-background-radius: 5;
                -fx-border-color: #00e5ff;
                -fx-border-radius: 5;
                -fx-border-width: 1;
    """);

            headingInput.getEditor().setStyle("""
                -fx-text-fill: white;
                -fx-background-color: #1b1b1b;
                -fx-prompt-text-fill: gray;
    """);


        final boolean[] isFiltering = {false};
        final boolean[] isSelecting={false};


        headingInput.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (isFiltering[0] || isSelecting[0]) return;  // <-- IMPORTANT

            isFiltering[0] = true;

            if (newText.isBlank() || newText.length() < oldText.length()) {
                filteredOptions.setPredicate(e -> true);
            }

            String filter = newText.toLowerCase();

            filteredOptions.setPredicate(item ->
                    filter.isEmpty() || item.toLowerCase().contains(filter)
            );

            if (!filteredOptions.isEmpty() && !headingInput.isShowing()) {
                headingInput.show();
            } else if (filteredOptions.isEmpty()) {
                headingInput.hide();
            }

            isFiltering[0] = false;
        });



        headingInput.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                isSelecting[0] = true;

                headingInput.getEditor().setText(newVal);
                headingInput.getEditor().positionCaret(newVal.length());

                filteredOptions.setPredicate(s -> true);

                isSelecting[0] = false;
            }
        });




        applyButton = new Button("Apply Changes");
        applyButton.setStyle(
                "-fx-background-color: #3c6e71; -fx-text-fill: white; -fx-font-weight: bold;"
        );
        applyButton.setMaxWidth(Double.MAX_VALUE);
        applyButton.setOnAction(event -> handelButton());


        modelStage=new Stage();
        modelButton = new Button("Model data");
        modelButton.setStyle(
                "-fx-background-color: #3c6e71; -fx-text-fill: white; -fx-font-weight: bold;"
        );
        modelButton.setMaxWidth(Double.MAX_VALUE);
        modelButton.setOnAction(event -> {
            new ModelView(modelStage,selected.getType());
        });


        inputBox = new VBox(8, altitudeField, speedField, degField, headingInput, applyButton);
        inputBox.setPadding(new Insets(10, 0, 0, 0));

        controlPanel.getChildren().addAll(controlTitle, aircraftScroll, inputBox);




        var root = new HBox(terminalAndCanvas,controlPanel);
        root.setMinWidth(1280);
        root.setMinHeight(720);

        Scene scene=new Scene(root);
        scene.setOnKeyPressed(event ->{
            switch (event.getCode()){
                case ESCAPE:
                    gameController.changeFlightAsPlayerInput("stop");
                    stage.close();
                    modelStage.close();
                    break;
            }
        });

        return scene;
    }



    private TextField createStyledTextField(String placeholder) {
        TextField tf = new TextField();
        tf.setPromptText(placeholder);
        tf.setStyle(
                "-fx-background-color: #333333;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: gray;" +
                        "-fx-background-radius: 5;"
        );
        return tf;
    }

    public void render() {
        //Draws the background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1280, 720);

        //Draws the airport
        AirPort airPort=gameController.airPort;

        gc.setStroke(Color.WHITE);

        for (Runway runway: airPort.getRunways()){
            List<Point> point=runway.getEndPoints();

            gc.strokeLine(point.get(0).getX(),point.get(0).getY()
                ,point.get(1).getX(),point.get(1).getY()
            );

            gc.strokeText(point.get(0).getName(),point.get(0).getX()-20,point.get(0).getY()-20);
            gc.strokeText(point.get(1).getName(),point.get(1).getX()-20,point.get(1).getY()-20);
        }

        //draws the dest points and assist points
        gc.setFill(Color.WHITE);
        for (Point point:airPort.getNearbyPoints()){
            gc.fillRect(point.getX(), point.getY(), 2,5);
            gc.fillText(point.getName(), point.getX()-15, point.getY()-10 );
        }


        //Draws the planes
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);

        synchronized (gameController.flights) {
            for (var flight : gameController.flights) {
                if (flight.getState()!= Flight.State.WaitingForTakeOff){
                    Point2D position = flight.getPosition();
                    gc.strokeOval(position.getX()-5, position.getY()-5, 10, 10);
                    gc.fillText(flight.toString(), position.getX() - 30, position.getY() + 30, 100);

                    Point2D direction= VectorUtils.getNormalizedDirVector(flight.getCurrDeg()).multiply(600).add(position);
                    gc.strokeLine(position.getX(),position.getY(),direction.getX(),direction.getY());
                }
            }
            updateAircraftList();
        }

        if (selected==null){
            inputBox.getChildren().remove(modelButton);
        }
    }




    public void updateAircraftList() {

        //puts in new flights
        for (Flight flight : gameController.flights) {
            if (!flightCards.containsKey(flight)) {
                VBox card = createFlightCard(flight);
                flightCards.put(flight, card);
                aircraftListContainer.getChildren().add(card);
            }
        }

        //removes flight
        flightCards.keySet().removeIf(f -> {
            if (!gameController.flights.contains(f)) {
                aircraftListContainer.getChildren().remove(flightCards.get(f));
                return true;
            }
            return false;
        });

        for (Flight flight : gameController.flights) {
            updateFlightCard(flight);
        }
    }

    private void updateFlightCard(Flight flight) {
        VBox card = flightCards.get(flight);
        if (card == null) return;


        String alltitude = (flight.getAssignedAltitude()>flight.getCurrAltitude()?
                " ↑ "+flight.getAssignedAltitude() : flight.getCurrAltitude()> flight.getAssignedAltitude()?
                " ↓ "+flight.getAssignedAltitude() : "");

        String deg = (flight.getCurrDeg()<flight.getAssignedDeg()?
                " ↑ "+flight.getAssignedDeg() : flight.getCurrDeg()> flight.getAssignedDeg()?
                " ↓ "+flight.getAssignedDeg() : "");

        String speed = (flight.getCurrSpeed()<flight.getAssignedSpeed()?
                " ↑ "+flight.getAssignedSpeed() : flight.getCurrSpeed()> flight.getAssignedSpeed()?
                " ↓ "+flight.getAssignedSpeed() : "");

        for (Node node : card.getChildren()) {
            if (node instanceof Label label) {
                String text = label.getText();
                if (text.startsWith("Speed:"))
                    label.setText("Speed: " + flight.getCurrSpeed()+speed);
                else if (text.startsWith("Altitude:"))
                    label.setText("Altitude: " + flight.getCurrAltitude()+alltitude);
                else if (text.startsWith("Deg:"))
                    label.setText(String.format("Deg: %.2f", flight.getCurrDeg())+deg);
                else if (text.startsWith("Heading:")) {
                    label.setText("Heading: " + (flight.getHeading()!=null ? flight.getHeading().getName():"none"));
                }
            }
        }

        if (flight == selected) {
            if (flight.isArriving()){
                card.setStyle("-fx-background-color: #563521; -fx-background-radius: 8; -fx-border-color: #555;");
            }else {
                card.setStyle("-fx-background-color: #41572f; -fx-background-radius: 8; -fx-border-color: #555;");
            }
        } else {
            if (flight.isArriving()){
                card.setStyle("-fx-background-color: #cb601c; -fx-background-radius: 8; -fx-border-color: #555;");
            }else {
                card.setStyle("-fx-background-color: #5cbd11; -fx-background-radius: 8; -fx-border-color: #555;");
            }
        }
    }

    private VBox createFlightCard(Flight flight) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(8));

        card.setPickOnBounds(true);
        card.setOnMouseClicked(event -> {
            if (selected==null){
                inputBox.getChildren().add(modelButton);
            }

            this.selected=flight;
            altitudeField.setText(String.valueOf(flight.getAssignedAltitude()));
            speedField.setText(String.valueOf(flight.getAssignedSpeed()));
            degField.setText(String.valueOf(flight.getAssignedDeg()));
            headingInput.getEditor().setText(flight.getHeading() != null ? flight.getHeading().getName() : "");

        });

        if (flight.isArriving()){
            card.setStyle(
                    "-fx-background-color: #cb601c;"+
                            "-fx-background-radius: 8; " +
                            "-fx-border-color: #555; " +
                            "-fx-border-radius: 8;"
            );
        }else {
            card.setStyle(
                    "-fx-background-color: #5cbd11;"+
                            "-fx-background-radius: 8; " +
                            "-fx-border-color: #555; " +
                            "-fx-border-radius: 8;"
            );
        }



        Label nameLabel = new Label("Flight: " + flight.getId());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label speedLabel = new Label("Speed: " + flight.getCurrSpeed());
        speedLabel.setStyle("-fx-text-fill: white;");

        Label altLabel = new Label("Altitude: " + flight.getCurrAltitude());
        altLabel.setStyle("-fx-text-fill: white;");

        Label degLabel = new Label(
                String.format("Deg: %.2f", flight.getCurrDeg())
        );
        degLabel.setStyle("-fx-text-fill: white;");

        Label headingLabel=new Label("Heading: " + (flight.getHeading()!=null ? flight.getHeading().getName():"none"));
        headingLabel.setStyle("-fx-text-fill: white;");

        Label modelLabel = new Label("Model: " + flight.getType().name);
        degLabel.setStyle("-fx-text-fill: white;");

        card.getChildren().addAll(nameLabel, speedLabel, altLabel, degLabel,headingLabel,modelLabel);
        return card;
    }



    private void handelApply(String command){
        if (command == null || command.isBlank()) return;

        updateHistory(command);
        gameController.changeFlightAsPlayerInput(command.trim());

        commandInput.clear();
    }

    private void handelButton(){
        String altitude=altitudeField.getText();
        String speed=speedField.getText();
        String deg=degField.getText();
        String heading= headingInput.getValue();

        String command=selected.getId()+" ";
        if (altitude!=null && !altitude.isBlank()){
            command+="A:"+altitude + " ";
        }
        if (speed!=null && !speed.isBlank()){
            command+="S:"+speed+" ";
        }
        if (deg!=null && !deg.isBlank()){
            command+="D:"+deg+" ";
        }
        if (heading!=null && !heading.isBlank()){
            command+="H:"+heading+" ";
        }

        handelApply(command.strip());
    }

    public void updateHistory(String command){
        terminalHistory.appendText(command + "\n");
        terminalHistory.setScrollTop(Double.MAX_VALUE);
    }
}
