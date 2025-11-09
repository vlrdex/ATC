package org.example;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.Controller.ACModelController;
import org.example.Controller.GameController;
import org.example.Model.AirPort;
import org.example.Model.Flight;
import org.example.Model.Point;
import org.example.Model.Runway;
import org.example.View.GameView;

import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {



    @Override
    public void start(Stage stage) {
        GameView gameView= new GameView(stage);
        GameController gameController= GameController.getInstance(gameView);
        ACModelController acModelController=ACModelController.getInstance();

        gameController.airPort=AirPort.getAirportByName("Bravo Regional");


        gameController.flights.add(new Flight(0,220,15000,
                acModelController.getRandom(),new Point2D(500,600)));
        gameController.flights.add(new Flight(279,320,15000,
                acModelController.getRandom(),new Point2D(1200,300)));
        gameController.flights.add(new Flight(90,180,15000,
                acModelController.getRandom(),new Point2D(100,200)));

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