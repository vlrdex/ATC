package org.example.Controller;

import org.example.Model.AirPort;
import org.example.Model.Flight;
import org.example.View.GameView;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    public List<Flight> flights = new ArrayList<>();
    public AirPort airPort;
    private boolean isRunning=false;
    public GameView gameView;

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

                synchronized (flights){
                    for (Flight flight : flights) {
                        flight.move();
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
                            int value=Integer.parseInt(commands[i].split(":")[1]);
                            switch (type){
                                case "A":
                                    flight.setAssignedAltitude(value);
                                    break;
                                case "S":
                                    flight.setAssignedSpeed(value);
                                    break;
                                case "D":
                                    flight.setAssignedDeg(value);
                                    break;

                            }
                        }
                        break;

                    }
                }
            }
        }

        
    }




}
