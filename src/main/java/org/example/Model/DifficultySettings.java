package org.example.Model;

public class DifficultySettings {
    private int maxFlights;
    private int maxArrivingFlights;
    private int maxDepartingFlights;

    public int currArrivingFlights;
    public int currDepartingFlights;

    public DifficultySettings(int maxFlights, int maxArrivingFlights, int maxDepartingFlights) {
        this.maxFlights = maxFlights;
        this.maxArrivingFlights = maxArrivingFlights;
        this.maxDepartingFlights = maxDepartingFlights;
    }

    public DifficultySettings(){
        this.maxFlights=10;
        this.maxArrivingFlights=5;
        this.maxDepartingFlights=5;
    }

    public int getMaxFlights() {
        return maxFlights;
    }

    public void setMaxFlights(int maxFlights) {
        if (maxFlights < maxArrivingFlights+maxDepartingFlights){
            this.maxFlights=maxArrivingFlights+maxDepartingFlights;
        }else {
            this.maxFlights = maxFlights;
        }
    }

    public int getMaxArrivingFlights() {
        return maxArrivingFlights;
    }

    public void setMaxArrivingFlights(int maxArrivingFlights) {
        if (maxArrivingFlights+maxDepartingFlights>maxFlights){
            this.maxArrivingFlights=maxFlights-maxDepartingFlights;
        }else{
            this.maxArrivingFlights = maxArrivingFlights;
        }
    }

    public int getMaxDepartingFlights() {
        return maxDepartingFlights;
    }

    public void setMaxDepartingFlights(int maxDepartingFlights) {
        if (maxArrivingFlights+maxDepartingFlights>maxFlights){
            this.maxDepartingFlights=maxFlights-maxArrivingFlights;
        }else{
            this.maxDepartingFlights = maxDepartingFlights;
        }
    }

    public static DifficultySettings medium(){
        DifficultySettings df=new DifficultySettings();
        df.maxFlights=8;
        df.maxArrivingFlights=4;
        df.maxDepartingFlights=4;
        df.currDepartingFlights=0;
        df.currArrivingFlights=0;

        return df;
    }

    @Override
    public String toString() {
        return "DifficultySettings{" +
                "maxFlights=" + maxFlights +
                ", maxArrivingFlights=" + maxArrivingFlights +
                ", maxDepartingFlights=" + maxDepartingFlights +
                ", currArrivingFlights=" + currArrivingFlights +
                ", currDepartingFlights=" + currDepartingFlights +
                '}';
    }
}
