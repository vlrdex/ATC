package org.example;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.Controller.GameController;
import org.example.Model.Flight;
import org.example.View.GameView;

/**
 * JavaFX App
 */
public class App extends Application {



    @Override
    public void start(Stage stage) {
        GameView gameView= new GameView(stage);
        GameController gameController= GameController.getInstance(gameView);


        gameController.flights.add(new Flight(0,180,15000,null,new Point2D(500,600)));
        gameController.flights.add(new Flight(279,320,15000,null,new Point2D(1200,300)));
        gameController.flights.add(new Flight(90,180,15000,null,new Point2D(100,200)));

        gameView.updateAircraftList();


        var gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gameController.start();
            }
        });

        gameThread.start();


    }

    public static void main(String[] args) {
        launch(args);
    }

}