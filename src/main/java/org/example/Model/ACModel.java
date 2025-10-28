package org.example.Model;

public class ACModel {
    public final String name;
    public final int topSpeed;
    public final int maxAltitude;
    public final int climbRate;
    public final int accRate;

    public ACModel(String name, int topSpeed, int maxAltitude, int climbRate, int accRate) {
        this.name = name;
        this.topSpeed = topSpeed;
        this.maxAltitude = maxAltitude;
        this.climbRate = climbRate;
        this.accRate = accRate;
    }
}
