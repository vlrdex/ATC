package org.example.Model;

import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirPortTest {

    private AirPort airPort;
    private Point r1;
    private Point r2;

    @BeforeEach
    void setup() {

        r1 = new Point("R1", new Point2D(0,0), Point.Type.runway);
        r2 = new Point("R2", new Point2D(100,0), Point.Type.runway);

        Runway runway = new Runway(List.of(r1,r2));

        Point assist1 = new Point("R1-1", new Point2D(10,0), Point.Type.near_assist);
        Point assist2 = new Point("R1-2", new Point2D(20,0), Point.Type.far_assist);

        List<Point> nearby = new ArrayList<>();
        nearby.add(assist1);
        nearby.add(assist2);

        airPort = new AirPort("Test", List.of(runway), nearby);
    }

    @Test
    void getPointByNameShouldFindNearbyPoint() {
        Point result = airPort.getPointByName("R1-1");

        assertNotNull(result);
        assertEquals("R1-1", result.getName());
    }

    @Test
    void getPointByNameShouldFindRunwayPoint() {
        Point result = airPort.getPointByName("R1");

        assertEquals(r1, result);
    }

    @Test
    void getOppositeRunwayPointShouldReturnOtherEnd() {

        Point result = airPort.getOppositeRunWayPoint(r1);

        assertEquals(r2, result);
    }

    @Test
    void getAssistPointsShouldReturnPair() {

        Pair<Point,Point> pair = airPort.getAssistPoints(r1);

        assertNotNull(pair);
        assertEquals(Point.Type.near_assist, pair.getKey().getType());
        assertEquals(Point.Type.far_assist, pair.getValue().getType());
    }

    @Test
    void getRunwayWithPointShouldReturnRunway() {

        Runway runway = airPort.getRunwayWithPoint(r1);

        assertNotNull(runway);
        assertTrue(runway.getEndPoints().contains(r1));
    }

    @Test
    void getRunwayWithPointShouldReturnNullForNonRunwayPoint() {

        Point dest = new Point("D", new Point2D(5,5), Point.Type.dest);

        Runway runway = airPort.getRunwayWithPoint(dest);

        assertNull(runway);
    }

}