package org.example.Model.Utils;

import javafx.geometry.Point2D;

public class VectorUtils {

    public static double calculateAngelWithDirection(Point2D first, Point2D second){
        final double deg=Math.toRadians(first.angle(second));
        final double sin=Math.sin(deg);
        final double cos=Math.cos(deg);

        final double x=first.getX();
        final double y=first.getY();

        final Point2D rotated=new Point2D(x*cos-sin*y,sin*x+cos*y);
        if (rotated.angle(second)<=1){
            return Math.toDegrees(deg);
        }else {
            return -Math.toDegrees(deg);
        }
    }


    //it rotates it with 90 deg to match my system
    public static Point2D getNormalizedDirVector(double deg){
        return new Point2D(Math.cos(Math.toRadians(deg-90)),Math.sin(Math.toRadians(deg-90)));
    }
}
