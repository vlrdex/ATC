package org.example.Model;

import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.example.Controller.GameController;
import org.example.Model.Utils.VectorUtils;
import org.example.View.GameView;

import java.util.Random;

public class Flight {
    private String id;
    //between 0 and 359
    private double currDeg;
    private double assignedDeg;

    //in knots
    private int currSpeed;
    private int assignedSpeed;

    //in feet
    private int currAltitude;
    private int assignedAltitude;
    private final ACModel type;
    private Point2D position;

    private Point heading;
    private boolean atDirection=false;
    private State state;

    //TODO maradék adatokkal kiegésziteni


    //generáláshoz szükséges adatok
    private static final String[] AIRLINE_CODES = {
            "AFR", "DLH", "BAW", "RYN", "KLM", "SWR", "IBE", "EZY", "WZZ", "AUA"
    };
    private static final Random random = new Random(12);


    public Flight(int currDeg, int assignedDeg, int currSpeed, int assignedSpeed, int currAltitude, int assignedAltitude, ACModel type, Point2D position,State state) {
        this.id=generateId();
        this.currDeg = currDeg;
        this.assignedDeg = assignedDeg;
        this.currSpeed = currSpeed;
        this.assignedSpeed = assignedSpeed;
        this.currAltitude = currAltitude;
        this.assignedAltitude = assignedAltitude;
        this.type = type;
        this.position = position;
        this.heading=null;
        this.state=state;
    }

    public Flight(int currDeg, int currSpeed, int currAltitude, ACModel type, Point2D position,State state) {
        this.id=generateId();
        this.currDeg = currDeg;
        this.assignedDeg=currDeg;
        this.currSpeed = currSpeed;
        this.assignedSpeed=currSpeed;
        this.currAltitude = currAltitude;
        this.assignedAltitude=currAltitude;
        this.type = type;
        this.position = position;
        this.state=state;
    }

    private String generateId(){
        String airline = AIRLINE_CODES[random.nextInt(AIRLINE_CODES.length)];
        int flightNumber = 100 + random.nextInt(9000);
        return airline + flightNumber;
    }

    public void move(){
        switch (this.state){
            case WaitingForTakeOff -> {
                break;
            }
            case TakeOff -> {
                takeOff();
                break;
            }
            default -> {
                adjustCurrentData();
                break;
            }

        }
    }


    private void adjustPosition(){
        Point2D vector = VectorUtils.getNormalizedDirVector(currDeg);

        vector = vector.multiply((double) currSpeed/300);
        this. position=this.position.add(vector);
    }



    private void adjustCurrentData(){

        adjustPosition();

        if (!atDirection && (currDeg!=assignedDeg || heading!=null) ){
            double distance=0;
            Point2D directionalVector=VectorUtils.getNormalizedDirVector(currDeg);
            Point2D destVector;

            if (heading != null){
                destVector=this.heading.getPoint2D().subtract(this.position).normalize();
            }else {
                destVector=VectorUtils.getNormalizedDirVector(assignedDeg);
            }


            distance= VectorUtils.calculateAngelWithDirection(directionalVector,destVector);


            if (distance>0){
                if(distance<1.5){
                    if (heading!=null){
                        assignedDeg=currDeg;
                        atDirection=true;
                        currDeg+=distance;
                    }else{
                        currDeg=assignedDeg;
                    }
                }else {
                    currDeg+=1.5;

                    if (currDeg>=360){
                        currDeg-=360;
                    }
                }

            }else {
                if(Math.abs(distance)<1.5 ){
                    if (heading!=null){
                        assignedDeg=currDeg;
                        atDirection=true;
                        currDeg+=distance;
                    }else{
                        currDeg=assignedDeg;
                    }
                }else {
                    currDeg-=1.5;
                    if (currDeg<0){
                        currDeg+=360;
                    }
                }
            }

        }



        if (currSpeed!=assignedSpeed){

            if (currSpeed<assignedSpeed){
                currSpeed+=type.accRate/3;
                if (currSpeed>assignedSpeed){
                    currSpeed=assignedSpeed;
                }

            }else {

                currSpeed-=type.accRate/3;
                if (currSpeed<assignedSpeed){
                    currSpeed=assignedSpeed;
                }
            }
        }


        if (currAltitude!=assignedAltitude){

            if (currAltitude<assignedAltitude){
                currAltitude+=type.climbRate;
                if (currAltitude>assignedAltitude){
                    currAltitude=assignedAltitude;
                }

            }else {
                currAltitude-=type.climbRate;
                if (currAltitude<assignedAltitude){
                    currAltitude=assignedAltitude;
                }
            }

        }


    }

    private void takeOff(){
        adjustPosition();
        this.currSpeed+=this.type.accRate/3;
        if (this.currSpeed>=100){
            this.currAltitude+=(this.type.climbRate/2);
        }
        if (currSpeed>=160){
            this.state=State.Departing;
        }
    }



    public String getId(){
        return this.id;
    }

    public double getCurrDeg() {
        return currDeg;
    }

    public double getAssignedDeg() {
        return assignedDeg;
    }

    public void setAssignedDeg(double deg){
        if (this.state!=State.WaitingForTakeOff){
            if (deg < 0){
                this.assignedDeg=0;
            } else if (deg > 359) {
                this.assignedDeg=359;
            }else {
                this.assignedDeg=deg;
            }
        }
    }

    public int getCurrSpeed() {
        return currSpeed;
    }

    public int getAssignedSpeed() {
        return assignedSpeed;
    }

    public void setAssignedSpeed(int speed){
        if (speed> this.type.topSpeed ){
            this.assignedSpeed = this.type.topSpeed;
        }else if (speed< 160){
            this.assignedSpeed=160;
        }else {
            this.assignedSpeed=speed;
        }
    }

    public int getCurrAltitude() {
        return currAltitude;
    }

    public int getAssignedAltitude() {
        return assignedAltitude;
    }

    public void setAssignedAltitude(int altitude){
        if (altitude > this.type.maxAltitude ){
            this.assignedAltitude = this.type.maxAltitude;
        } else if (altitude < 1000) {
            this.assignedAltitude=1000;
        }else {
            this.assignedAltitude=altitude;
        }
    }

    public ACModel getType() {
        return type;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D p){
        this.position=p;
    }

    public Point getHeading() {
        return heading;
    }

    public void setHeading(Point heading) {

        switch (state){
            case WaitingForTakeOff -> {
                if (heading.getType()!= Point.Type.runway){
                    GameController.getInstance().gameView.updateHistory("Invalid point for take off!");
                }else {
                    Point spawn=GameController.getInstance().airPort.getOppositeRunWayPoint(heading);

                    Point2D dirVector=new Point2D(heading.getX()-spawn.getX(), heading.getY()-spawn.getY()).normalize();
                    Point2D baseVector=VectorUtils.getNormalizedDirVector(0);

                    Double dist=VectorUtils.calculateAngelWithDirection(baseVector,dirVector);
                    if (dist<0){
                        this.currDeg=dist+360;
                        this.assignedDeg=this.currDeg;
                    }else {
                        this.currDeg=dist;
                        this.assignedDeg=this.currDeg;
                    }
                    this.position=spawn.getPoint2D();
                    this.state=State.TakeOff;
                }
            }
            case Arriving -> {
                if (heading.getType()== Point.Type.runway && assignedSpeed ==160 && currAltitude<=3000 && assignedAltitude==1000){
                    Pair<Point,Point> pair=GameController.getInstance().airPort.getAssistPoints(heading);


                    if (pair!=null){
                        if (VectorUtils.calculateIfPointIsInside(pair.getKey().getPoint2D(), pair.getValue().getPoint2D(), this.getPosition())){
                            this.state=State.Landing;
                        }
                    }
                }


                    this.heading = heading;
                    this.atDirection=false;

            }
            default -> {
                this.heading = heading;
                this.atDirection=false;
            }

        }

    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isArriving(){
        return state==State.Arriving || state==State.Landing || state==State.Landed;
    }

    @Override
    public String toString(){
        return id+" alt: "+currAltitude+"\nspd: "+currSpeed+" deg: "+currDeg;
    }

    public enum State{
        Arriving,
        Landing,
        Landed,
        Departing,
        TakeOff,
        WaitingForTakeOff,
        AtDest
    }
}
