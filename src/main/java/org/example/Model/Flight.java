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
    private ACModell type;
    private Point2D position;

    //TODO maradék adatokkal kiegésziteni


    //generáláshoz szükséges adatok
    private static final String[] AIRLINE_CODES = {
            "AFR", "DLH", "BAW", "RYN", "KLM", "SWR", "IBE", "EZY", "WZZ", "AUA"
    };
    private static final Random random = new Random(12);


    public Flight(int currDeg, int assignedDeg, int currSpeed, int assignedSpeed, int currAltitude, int assignedAltitude, ACModell type, Point2D position) {
        this.id=generateId();
        this.currDeg = currDeg;
        this.assignedDeg = assignedDeg;
        this.currSpeed = currSpeed;
        this.assignedSpeed = assignedSpeed;
        this.currAltitude = currAltitude;
        this.assignedAltitude = assignedAltitude;
        this.type = type;
        this.position = position;
    }

    public Flight(int currDeg, int currSpeed, int currAltitude, ACModell type, Point2D position) {
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
        vector = vector.multiply((double) currSpeed/100);
        this. position=this.position.add(vector);

        //TODO kiszervezni ezt egy fügvénybe hogy átláthatobb legyen és frisiteni a modell adataival
        if (currDeg!=assignedDeg){
            if (currDeg<assignedDeg){
                currDeg+=15;
                if(currDeg>assignedDeg){
                    currDeg=assignedDeg;
                }
            }else {
                currDeg-=15;
                if(currDeg<assignedDeg){
                    currDeg=assignedDeg;
                }
            }
        }
        if (currSpeed!=assignedSpeed){
            if (currDeg<assignedSpeed){
                currSpeed+=1;
                if (currSpeed>assignedSpeed){
                    currSpeed=assignedSpeed;
                }
            }else {
                currSpeed-=1;
                if (currSpeed<assignedSpeed){
                    currSpeed=assignedSpeed;
                }
            }
        }
        if (currAltitude!=assignedAltitude){
            if (currAltitude<assignedAltitude){
                currAltitude+=150;
                if (currAltitude>assignedAltitude){
                    currAltitude=assignedAltitude;
                }
            }else {
                currAltitude-=150;
                if (currAltitude<assignedAltitude){
                    currAltitude=assignedAltitude;
                }
            }
        }

    }

    private String generateId(){
        String airline = AIRLINE_CODES[random.nextInt(AIRLINE_CODES.length)];
        int flightNumber = 100 + random.nextInt(9000); // 3–4 digit number
        return airline + flightNumber;
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
        if (speed> 320 /*this.type.topSpeed*/){
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
        if (altitude > 20000 /*this.type.maxAltitude*/){
            this.assignedAltitude = this.type.maxAltitude;
        } else if (altitude < 1000) {
            this.assignedAltitude=1000;
        }else {
            this.assignedAltitude=altitude;
        }
    }

    public ACModell getType() {
        return type;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D p){
        this.position=p;
    }

    @Override
    public String toString(){
        return id+" alt: "+currAltitude+"\nspd: "+currSpeed+" deg: "+currDeg;
    }
}
