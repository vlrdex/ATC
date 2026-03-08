package org.example.Model;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void constructorAndGettersShouldWork() {
        Point2D p = new Point2D(10, 20);
        Point point = new Point("A", p, Point.Type.runway);

        assertEquals("A", point.getName());
        assertEquals(p, point.getPoint2D());
        assertEquals(Point.Type.runway, point.getType());
    }

    @Test
    void getXandYShouldReturnCoordinates() {
        Point point = new Point("A", new Point2D(5, 8), Point.Type.dest);

        assertEquals(5, point.getX());
        assertEquals(8, point.getY());
    }

    @Test
    void equalsShouldReturnTrueForSameValues() {
        Point p1 = new Point("A", new Point2D(1,2), Point.Type.runway);
        Point p2 = new Point("A", new Point2D(1,2), Point.Type.runway);

        assertEquals(p1, p2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentName() {
        Point p1 = new Point("A", new Point2D(1,2), Point.Type.runway);
        Point p2 = new Point("B", new Point2D(1,2), Point.Type.runway);

        assertNotEquals(p1, p2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentType() {
        Point p1 = new Point("A", new Point2D(1,2), Point.Type.runway);
        Point p2 = new Point("A", new Point2D(1,2), Point.Type.dest);

        assertNotEquals(p1, p2);
    }

}