package org.example.Model;

import java.util.List;

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
}
