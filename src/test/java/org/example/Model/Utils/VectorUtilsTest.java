package org.example.Model.Utils;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorUtilsTest {

    @Test
    void getNormalizedDirVectorShouldReturnUnitVector() {

        Point2D v = VectorUtils.getNormalizedDirVector(90);

        double length = v.magnitude();

        assertEquals(1.0, length, 0.0001);
    }

    @Test
    void getNormalizedDirVectorShouldRotate90Degrees() {

        Point2D v = VectorUtils.getNormalizedDirVector(90);

        assertEquals(1.0, v.getX(), 0.0001);
        assertEquals(0.0, v.getY(), 0.0001);
    }

    @Test
    void angleToShouldReturnCorrectAngle() {

        Point2D a = new Point2D(0,0);
        Point2D b = new Point2D(1,0);

        double angle = VectorUtils.angleTo(a,b);

        assertEquals(0, angle, 0.0001);
    }

    @Test
    void angleToShouldReturn90Degrees() {

        Point2D a = new Point2D(0,0);
        Point2D b = new Point2D(0,1);

        double angle = VectorUtils.angleTo(a,b);

        assertEquals(90, angle, 0.0001);
    }

    @Test
    void angleToShouldReturn180Degrees() {

        Point2D a = new Point2D(0,0);
        Point2D b = new Point2D(-1,0);

        double angle = VectorUtils.angleTo(a,b);

        assertEquals(180, angle, 0.0001);
    }

    @Test
    void calculateAngelWithDirectionShouldReturnZeroForSameVector() {

        Point2D v1 = new Point2D(1,0);
        Point2D v2 = new Point2D(1,0);

        double angle = VectorUtils.calculateAngelWithDirection(v1,v2);

        assertEquals(0, angle, 0.0001);
    }

    @Test
    void calculateIfPointIsInsideShouldReturnTrueForInsidePoint() {

        Point2D assist1 = new Point2D(0,0);
        Point2D assist2 = new Point2D(100,0);

        Point2D inside = new Point2D(50,10);

        boolean result = VectorUtils.calculateIfPointIsInside(assist1,assist2,inside);

        assertTrue(result);
    }

    @Test
    void calculateIfPointIsInsideShouldReturnFalseForOutsidePoint() {

        Point2D assist1 = new Point2D(0,0);
        Point2D assist2 = new Point2D(100,0);

        Point2D outside = new Point2D(300,300);

        boolean result = VectorUtils.calculateIfPointIsInside(assist1,assist2,outside);

        assertFalse(result);
    }

    @Test
    void getRandomPointForSpawningShouldStayOnBorders() {

        for(int i=0;i<1000;i++) {

            Point2D p = VectorUtils.getRandomPointForSpawning();

            boolean onTop = Math.abs(p.getY() - 5) < 0.0001;
            boolean onBottom = Math.abs(p.getY() - (720 - 5)) < 0.0001;
            boolean onLeft = Math.abs(p.getX() - 5) < 0.0001;
            boolean onRight = Math.abs(p.getX() - (1280 - 5)) < 0.0001;

            assertTrue(onTop || onBottom || onLeft || onRight);
        }
    }

}