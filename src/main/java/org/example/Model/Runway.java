package org.example.Model;


import java.util.List;

public class Runway {
    private List<Point> endPoints;
    private List<Point> assistPoints;

    public Runway(List<Point> endPoints, List<Point> assistPoints) {
        this.endPoints = endPoints;
        this.assistPoints = assistPoints;
    }

    public List<Point> getEndPoints() {
        return endPoints;
    }

    public List<Point> getAssistPoints() {
        return assistPoints;
    }
}
