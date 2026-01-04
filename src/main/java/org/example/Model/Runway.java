package org.example.Model;


import javafx.geometry.Point2D;

import java.util.List;

public class Runway {
    private List<Point> endPoints;

    //point from the first point to the direction of the second point
    private Point2D dirVector;

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
}
