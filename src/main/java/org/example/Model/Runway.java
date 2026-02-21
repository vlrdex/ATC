package org.example.Model;


import javafx.geometry.Point2D;
import org.example.Model.Utils.VectorUtils;

import java.util.List;

public class Runway {
    private List<Point> endPoints;

    //point from the first point to the direction of the second point
    private Point2D dirVector;

    private Boolean isOccupied;

    public Runway(List<Point> endPoints){
        this.endPoints = endPoints;
    }

    public List<Point> getEndPoints() {
        return endPoints;
    }

    public Point2D getDirVector() {
        return dirVector;
    }

    public void setDirVector(Point2D dirVector) {
        this.dirVector = dirVector;
    }

    public Boolean getOccupied() {
        return isOccupied;
    }

    public void setOccupied(Boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isWindWrong(Wind wind,Point point){

        Point2D vector;
        if (point.getPoint2D()!=endPoints.get(0).getPoint2D())
        {
            vector = new Point2D(endPoints.get(0).getX()-endPoints.get(1).getX(),endPoints.get(0).getY()-endPoints.get(1).getY()).normalize();
        }else
        {
            vector = new Point2D(endPoints.get(1).getX()-endPoints.get(0).getX(),endPoints.get(1).getY()-endPoints.get(0).getY()).normalize();
        }

        Point2D zeroVector = VectorUtils.getNormalizedDirVector(0);

        double deg = VectorUtils.angleTo(zeroVector,vector);

        double diff= Math.abs(wind.getDirection()-deg);

        if (diff <= 15)
        {
            return  true;
        }
        return false;

    }
}
