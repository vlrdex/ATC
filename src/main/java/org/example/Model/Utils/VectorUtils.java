package org.example.Model.Utils;

import javafx.geometry.Point2D;

import java.util.Random;

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
        Point2D A=assist1.add(rotatedVector.multiply(50));
        Point2D B=A.add(vector.multiply(160));
        Point2D D=assist1.add(rotatedVector.multiply(-50));

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

    public static Point2D getRandomPointForSpawning() {
        Random rand = new Random();

        int width = 1280;
        int height = 720;
        int border = 5;

        int side = rand.nextInt(4); // 0=top, 1=bottom, 2=left, 3=right

        double x = 0;
        double y = 0;

        switch (side) {
            case 0: // top
                x = border + rand.nextDouble() * (width - 2 * border);
                y = border;
                break;
            case 1: // bottom
                x = border + rand.nextDouble() * (width - 2 * border);
                y = height - border;
                break;
            case 2: // left
                x = border;
                y = border + rand.nextDouble() * (height - 2 * border);
                break;
            case 3: // right
                x = width - border;
                y = border + rand.nextDouble() * (height - 2 * border);
                break;
        }

        return new Point2D(x, y);
    }

    public static double angleTo(Point2D from, Point2D to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();

        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);

        if (angleDeg < 0) angleDeg += 360; // normalize to 0–359
        return angleDeg;
    }






}
