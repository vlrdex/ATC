package org.example.Controller;

import javafx.geometry.Point2D;
import org.example.Model.*;
import org.example.Model.Utils.VectorUtils;
import org.example.View.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    private List<Flight> flights = new ArrayList<>();
    public AirPort airPort;
    private boolean isRunning=false;
    public GameView gameView;

    private DifficultySettings difficultySettings;

    private final Random random=new Random();
    private static GameController instace;


    private GameController(GameView gameView) {
        this.gameView = gameView;
    }

    public static GameController getInstance(GameView gameView){
        if (instace==null){
             instace = new GameController(gameView);
        }
        return instace;
    }

    public static GameController getInstance(){
        if (instace==null){
            instace = new GameController(null);
        }
        return instace;
    }

    public void setGameView(GameView gameView){
        this.gameView=gameView;
    }



    public void start(){
        isRunning=true;
        this.gameLoop();
    }

    public void stop(){
        isRunning=false;
    }

    private void gameLoop(){
        long lastUpdateTime = System.currentTimeMillis();
        long interval = 100;

        while (isRunning) {
            long currentTime = System.currentTimeMillis();


            if (currentTime - lastUpdateTime >= interval) {

                synchronized (flights){
                    if (flights.size()<difficultySettings.getMaxFlights()){
                        if (random.nextInt(100)==1){
                            flights.add(generateRandomFlight());
                        }
                    }

                    List<Flight> flightsToRemove= new ArrayList<>();


                    for (Flight flight : flights) {
                        if (flight.getState()== Flight.State.Landed || flight.getState()== Flight.State.AtDest){
                            flightsToRemove.add(flight);
                            continue;
                        }
                        flight.move();
                    }

                    for (Flight flight : flightsToRemove) {
                        flights.remove(flight);
                    }
                }



                lastUpdateTime = currentTime;
            }
        }
    }

    public void changeFlightAsPlayerInput(String command){
        if (command==null || command.isBlank()){return;}

        if (command.equals("stop")){
            isRunning=false;
        } else if (command.equals("resume")) {
            isRunning=true;
        }else {
            String[] commands=command.split(" ");
            String id = commands[0];

            synchronized (flights){
                for (Flight flight : flights){

                    if (flight.getId().equals(id)){

                        for (int i=1;i<commands.length;i++){

                            String type=commands[i].split(":")[0];
                            String value =commands[i].split(":")[1];

                            switch (type){
                                case "A":
                                    flight.setAssignedAltitude(Integer.parseInt(value));
                                    break;
                                case "S":
                                    flight.setAssignedSpeed(Integer.parseInt(value));
                                    break;
                                case "D":
                                    flight.setAssignedDeg(Double.parseDouble(value));
                                    break;
                                case "H":
                                    flight.setHeading(airPort.getPointByName(value));
                                    break;
                            }
                        }
                        break;
                    }

                }
            }

        }

        
    }

    private Flight generateRandomFlight(){
        if (difficultySettings.currDepartingFlights==difficultySettings.getMaxDepartingFlights()){
            return generateArriving();
        }
        if (difficultySettings.currArrivingFlights==difficultySettings.getMaxArrivingFlights()){
            return generateDeparting();
        }
        if (random.nextBoolean()){ // if true it generates departing flight;
            return generateDeparting();
        }else {
            return generateArriving();
        }
    }

    private Flight generateDeparting(){
        Flight flight = new Flight(0,0,0, ACModelController.getInstance().getRandom(), null, Flight.State.WaitingForTakeOff);
        flight.setDestination(airPort.getRandomDest());
        return flight;
    }

    private Flight generateArriving(){
        Point2D spawn= VectorUtils.getRandomPointForSpawning();
        double deg=VectorUtils.angleTo(spawn,new Point2D(
                100 + random.nextDouble() * (1280 - 200),
                100 + random.nextDouble() * (720 - 200)
        ));
        ACModel model= ACModelController.getInstance().getRandom();

        return new Flight(deg,(int)(160+random.nextDouble()*(model.topSpeed)),(int)(4000+random.nextDouble()*(model.maxAltitude)),model,spawn, Flight.State.Arriving);
    }


    public DifficultySettings getDifficultySettings() {
        return difficultySettings;
    }

    public void setDifficultySettings(DifficultySettings difficultySettings) {
        this.difficultySettings = difficultySettings;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void  addFlight(Flight flight){
        if (flight.isArriving()){
            difficultySettings.currArrivingFlights++;
        }else {
            difficultySettings.currDepartingFlights++;
        }
        flights.add(flight);
    }
}
