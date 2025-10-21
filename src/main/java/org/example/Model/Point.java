package org.example.Model;

import javafx.geometry.Point2D;

public class Point {
    private final String name;
    private final Point2D point2D;
    // TODO az enumot még hozá kell rakni


    public Point(String name, Point2D point2D) {
        this.name = name;
        this.point2D = point2D;
    }

    public String getName() {
        return name;
    }

    public Point2D getPoint2D() {
        return point2D;
    }
}
