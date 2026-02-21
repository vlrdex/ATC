package org.example.Model;

public class Stats {
    public int flightManaged;
    public int lands;
    public int atDest;
    public int wrongDis;
    public int correctDis;
    public int drasticWindChanges;
    public Stats() {
        this.flightManaged = 0;
        this.lands=0;
        this.atDest=0;
        this.wrongDis=0;
        this.correctDis=0;
        this.drasticWindChanges=0;
    }

    @Override
    public String toString() {
        return "Flight Managed: " + flightManaged +
                "\nLands: " + lands +
                "\nAt Destination: " + atDest +
                "\nWrong Distance: " + wrongDis +
                "\nCorrect Distance: " + correctDis +
                "\nDrastic Wind Changes: " + drasticWindChanges;
    }
}
