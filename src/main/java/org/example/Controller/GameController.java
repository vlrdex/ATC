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
    public final Stats stats = new Stats();

    private DifficultySettings difficultySettings;

    private final Random random=new Random();
    public final  Wind wind=new Wind(0,0);
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
        long interval = 1000;

        while (isRunning) {
            long currentTime = System.currentTimeMillis();


            if (currentTime - lastUpdateTime >= interval) {

                wind.change();

                synchronized (flights){
                    if (flights.size()<difficultySettings.getMaxFlights()){
                        if (random.nextInt(100)==1  || flights.isEmpty()){
                            flights.add(generateRandomFlight());
                        }
                    }

                    List<Flight> flightsToRemove= new ArrayList<>();

                    for (Flight flight: flights)
                    {
                        flight.isInDanger=false;
                        for (Flight other: flights)
                        {
                            if (other!= flight)
                            {
                                if (flight.getPosition().distance(other.getPosition())<30)
                                {
                                    if (Math.abs(flight.getCurrAltitude()-other.getCurrAltitude()) <2000)
                                    {
                                        stats.wrongDis++;
                                        flight.isInDanger=true;
                                    }
                                }
                            }
                        }
                    }


                    for (Flight flight : flights) {

                        if (flight.getState()== Flight.State.Landed || flight.getState()== Flight.State.AtDest){
                            flightsToRemove.add(flight);
                            continue;
                        }

                        flight.move();
                    }

                    for (Flight flight : flightsToRemove) {
                        if (flight.isArriving())
                        {
                            stats.lands++;
                            difficultySettings.currArrivingFlights--;
                        }else
                        {
                            stats.atDest++;
                            difficultySettings.currDepartingFlights--;
                        }
                        stats.flightManaged++;
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
        difficultySettings.currDepartingFlights++;
        return flight;
    }

    private Flight generateArriving(){
        Point2D spawn= VectorUtils.getRandomPointForSpawning();

        Point2D lookat=new Point2D(
                200 + random.nextDouble() * (1280 - 400),
                100 + random.nextDouble() * (720 - 200)
        );
        double deg=VectorUtils.angleTo(spawn,lookat);
        ACModel model= ACModelController.getInstance().getRandom();
        difficultySettings.currArrivingFlights++;

        return new Flight(deg+90,(int)(160+random.nextDouble()*(model.topSpeed)),(int)(4000+random.nextDouble()*(model.maxAltitude)),model,spawn, Flight.State.Arriving);
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
