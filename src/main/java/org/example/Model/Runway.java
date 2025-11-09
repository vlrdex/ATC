package org.example.Model;


import java.util.List;

public class Runway {
    private List<Point> endPoints;

    public Runway(List<Point> endPoints){
        this.endPoints = endPoints;
    }

    public List<Point> getEndPoints() {
        return endPoints;
    }

}
