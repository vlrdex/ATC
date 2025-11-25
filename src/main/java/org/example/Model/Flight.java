package org.example.Model;

import javafx.geometry.Point2D;

import java.util.Random;

public class Flight {
    private String id;
    //between 0 and 359
    private int currDeg;
    private int assignedDeg;

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

    //TODO maradék adatokkal kiegésziteni


    //generáláshoz szükséges adatok
    private static final String[] AIRLINE_CODES = {
            "AFR", "DLH", "BAW", "RYN", "KLM", "SWR", "IBE", "EZY", "WZZ", "AUA"
    };
    private static final Random random = new Random(12);


    public Flight(int currDeg, int assignedDeg, int currSpeed, int assignedSpeed, int currAltitude, int assignedAltitude, ACModel type, Point2D position) {
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
    }

    public Flight(int currDeg, int currSpeed, int currAltitude, ACModel type, Point2D position) {
        this.id=generateId();
        this.currDeg = currDeg;
        this.assignedDeg=currDeg;
        this.currSpeed = currSpeed;
        this.assignedSpeed=currSpeed;
        this.currAltitude = currAltitude;
        this.assignedAltitude=currAltitude;
        this.type = type;
        this.position = position;
    }

    public void move(){
        Point2D vector = new Point2D(Math.cos(Math.toRadians(this.currDeg-90)),Math.sin(Math.toRadians(this.currDeg-90)));
        //TODO kitalálni egy jobb közelités majd a sebeség becslésére az arányokktol fügöen
        vector = vector.multiply((double) currSpeed/300);
        this. position=this.position.add(vector);

        adjustCurrentData();
    }

    private String generateId(){
        String airline = AIRLINE_CODES[random.nextInt(AIRLINE_CODES.length)];
        int flightNumber = 100 + random.nextInt(9000);
        return airline + flightNumber;
    }

    private void adjustCurrentData(){

        if (!atDirection && (currDeg!=assignedDeg || heading!=null) ){
            int distance=0;
            Point2D directionalVector=new Point2D(Math.cos(Math.toRadians(this.currDeg-90)),Math.sin(Math.toRadians(this.currDeg-90)));
            Point2D destVector;

            if (heading != null){
                destVector=this.heading.getPoint2D().subtract(this.position).normalize();
            }else {
                destVector=new Point2D(Math.cos(Math.toRadians(this.assignedDeg-90)),Math.sin(Math.toRadians(this.assignedDeg-90)));
            }


            distance=calculateAngelWithDirection(directionalVector,destVector);

            System.out.println(distance);

            if (distance>0){
                if(distance<2){
                    if (heading!=null){
                        assignedDeg=currDeg;
                        atDirection=true;
                    }
                    currDeg=assignedDeg;
                }else {
                    currDeg+=2;

                    if (currDeg>=360){
                        currDeg-=360;
                    }
                }

            }else {
                if(Math.abs(distance)<2 ){
                    if (heading!=null){
                        assignedDeg=currDeg;
                        atDirection=true;
                    }
                    currDeg=assignedDeg;
                }else {
                    currDeg-=2;
                    if (currDeg<0){
                        currDeg+=360;
                    }
                }
            }

            System.out.println(currDeg);
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



    public String getId(){
        return this.id;
    }

    public int getCurrDeg() {
        return currDeg;
    }

    public int getAssignedDeg() {
        return assignedDeg;
    }

    public void setAssignedDeg(int deg){
        if (deg < 0){
            this.assignedDeg=0;
        } else if (deg > 359) {
            this.assignedDeg=359;
        }else {
            this.assignedDeg=deg;
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
        this.heading = heading;
        this.atDirection=false;
    }

    public int calculateAngelWithDirection(Point2D first,Point2D second){
        final double deg=Math.toRadians(first.angle(second));
        final double sin=Math.sin(deg);
        final double cos=Math.cos(deg);

        final double x=first.getX();
        final double y=first.getY();

        final Point2D rotated=new Point2D(x*cos-sin*y,sin*x+cos*y);
        if (rotated.angle(second)<=1){
            return (int)Math.toDegrees(deg);
        }else {
            return (int)-Math.toDegrees(deg);
        }
    }

    @Override
    public String toString(){
        return id+" alt: "+currAltitude+"\nspd: "+currSpeed+" deg: "+currDeg;
    }
}
