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
import java.util.Map;

/**
 * JavaFX App
 */
public class App extends Application {



    @Override
    public void start(Stage stage) {
        GameController gameController= GameController.getInstance();
        gameController.airPort=AirPort.getAirportByName("Bravo Regional");
        ACModelController acModelController=ACModelController.getInstance();




        gameController.flights.add(new Flight(40,0,0,
                acModelController.getRandom(),null, Flight.State.WaitingForTakeOff));
        gameController.flights.add(new Flight(359,320,15000,
                acModelController.getRandom(),new Point2D(1200,300), Flight.State.Arriving));
        gameController.flights.add(new Flight(310,180,15000,
                acModelController.getRandom(),new Point2D(100,200), Flight.State.Arriving));

        GameView gameView= new GameView(stage);
        gameView.updateAircraftList();
        gameController.setGameView(gameView);

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