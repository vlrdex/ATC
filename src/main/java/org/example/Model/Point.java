package org.example.Model;

import javafx.geometry.Point2D;

import java.util.Objects;

public class Point {
    private final String name;
    private final Point2D point2D;
    private final Type type;

    public enum Type{
        runway,
        near_assist,
        far_assist,
        dest
    }


    public Point(String name, Point2D point2D,Type type) {
        this.name = name;
        this.point2D = point2D;
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public Point2D getPoint2D() {
        return point2D;
    }

    public Type getType() {
        return type;
    }

    public double getX(){
        return point2D.getX();
    }

    public double getY(){
        return point2D.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(name, point.name) && Objects.equals(point2D, point.point2D) && type == point.type;
    }

}
