package org.example.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.example.Utils.ConfigLoader;
import org.example.Utils.Point2DAdapter;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AirPort {

    private String name;
    private List<Runway> runways;
    private List<Point> nearbyPoints;

    public AirPort(String name, List<Runway> runways, List<Point> nearbyPoints) {
        this.name = name;
        this.runways = runways;
        this.nearbyPoints = nearbyPoints;
    }

    public String getName() {
        return name;
    }

    public List<Runway> getRunways() {
        return runways;
    }

    public List<Point> getNearbyPoints() {
        return nearbyPoints;
    }

    public static AirPort getAirportByName(String name){
        try(FileReader fl= new FileReader(ConfigLoader.getProperty("AirPortFile"))){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Point2D.class,new Point2DAdapter())
                    .create();
            AirPort[] airPorts=gson.fromJson(fl, AirPort[].class);
            for (int i = 0; i < airPorts.length; i++) {
                if (airPorts[i].name.equals(name)){
                    AirPort airPort = airPorts[i];
                    for (Runway runway:airPort.runways){
                        //calculating assist points
                        Pair<Point,Point> pair=new Pair<>(runway.getEndPoints().get(0),runway.getEndPoints().get(1));
                        Point2D vector=new Point2D(pair.getKey().getX()-pair.getValue().getX(),pair.getKey().getY()-pair.getValue().getY());
                        vector = vector.normalize();
                        vector = vector.multiply(120);

                        Point2D ass1 = pair.getKey().getPoint2D().add(vector);
                        Point2D ass2 = ass1.add(vector);


                        Point2D ass3= pair.getValue().getPoint2D().subtract(vector);
                        Point2D ass4= ass3.subtract(vector);

                        Point R1A=new Point(pair.getKey().getName()+"-1",ass1, Point.Type.near_assist);
                        Point R2A=new Point(pair.getKey().getName()+"-2",ass2, Point.Type.far_assist);
                        Point L1A=new Point(pair.getValue().getName()+"-1",ass3, Point.Type.near_assist);
                        Point L2A=new Point(pair.getValue().getName()+"-2",ass4, Point.Type.far_assist);

                        airPort.getNearbyPoints().addAll(List.of(R1A,R2A,L1A,L2A));
                    }
                    return airPort;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public Point getPointByName(String name){
        for (Point point:this.nearbyPoints){
            if (point.getName().equals(name)){
                return point;
            }
        }

        for (Runway runway:this.runways){
            for (Point point : runway.getEndPoints()){
                if (point.getName().equals(name)){
                    return point;
                }
            }
        }

        return null;
    }
}
