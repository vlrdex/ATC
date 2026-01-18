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


    public static boolean calculateIfPointIsInside(Point2D assist1,Point2D assist2,Point2D underTest){

        Point2D vector = assist2.subtract(assist1).normalize();
        Point2D rotatedVector=new Point2D(-vector.getY(),vector.getX());
        Point2D A=assist1.add(rotatedVector.multiply(30));
        Point2D B=A.add(vector.multiply(140));
        Point2D D=assist1.add(rotatedVector.multiply(-30));

        Point2D AB=B.subtract(A);
        Point2D AD=D.subtract(A);
        Point2D AP=underTest.subtract(A);

        if (0<= AP.dotProduct(AB) && AP.dotProduct(AB) <= AB.dotProduct(AB)
            && 0<= AP.dotProduct(AD) && AP.dotProduct(AD) <=AD.dotProduct(AD)
        ){
            return true;
        }else {
            return false;
        }
    }





}
