package org.example.Model;

import org.example.Controller.GameController;

import java.util.Random;

public class Wind {

    private double direction; // degrees (0–360)
    private double speed;     // km/h
    private final Random random = new Random();

    private static final double DIRECTION_STD_DEV = 5.0;

    public Wind(double direction, double speed) {
        this.direction = normalizeDirection(direction);
        this.speed = speed;
    }

    public double getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }


    public void changeDirection() {

        double change = random.nextGaussian() * DIRECTION_STD_DEV;

        if (random.nextInt(1000) == 1)
        {
            change += random.nextDouble()*360;
        }

        if (change>=10)
        {
            GameController.getInstance().stats.drasticWindChanges++;
        }
        direction = normalizeDirection(direction + change);
    }

    public void changeSpeed() {
        double change = random.nextGaussian() * 2.0;
        speed = Math.max(0, speed + change);
        speed = Math.min(60,speed);
    }

    public void change()
    {
        changeDirection();
        changeSpeed();
    }

    private double normalizeDirection(double dir) {
        dir = dir % 360;
        if (dir < 0) dir += 360;
        return dir;
    }

    @Override
    public String toString() {
        return "Wind:\n" +
                "direction: " + String.format("%.2f", direction)+"\n"+
                "speed: " + String.format("%.2f", speed);
    }
}
