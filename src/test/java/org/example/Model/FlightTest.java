package org.example.Model;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    private Flight flight;
    private ACModel model;

    @BeforeEach
    void setup() {
        model = new ACModel("test",900, 12000, 30, 60);

        flight = new Flight(
                90,     // currDeg
                250,    // currSpeed
                5000,   // currAltitude
                model,
                new Point2D(100, 100),
                Flight.State.Arriving
        );

    }


    @Test
    void testIdGenerated() {
        assertNotNull(flight.getId());
        assertTrue(flight.getId().length() >= 4);
    }

    @Test
    void testInitialState() {
        assertEquals(Flight.State.Arriving, flight.getState());
    }


    @Test
    void testSetAssignedSpeedAboveMax() {
        flight.setAssignedSpeed(2000);
        assertEquals(model.topSpeed, flight.getAssignedSpeed());
    }

    @Test
    void testSetAssignedSpeedBelowMinimum() {
        flight.setAssignedSpeed(100);
        assertEquals(160, flight.getAssignedSpeed());
    }

    @Test
    void testSetAssignedSpeedNormal() {
        flight.setAssignedSpeed(300);
        assertEquals(300, flight.getAssignedSpeed());
    }


    @Test
    void testSetAssignedAltitudeAboveMax() {
        flight.setAssignedAltitude(50000);
        assertEquals(model.maxAltitude, flight.getAssignedAltitude());
    }

    @Test
    void testSetAssignedAltitudeBelowMin() {
        flight.setAssignedAltitude(200);
        assertEquals(1000, flight.getAssignedAltitude());
    }

    @Test
    void testSetAssignedAltitudeNormal() {
        flight.setAssignedAltitude(6000);
        assertEquals(6000, flight.getAssignedAltitude());
    }


    @Test
    void testMoveChangesPosition() {
        Point2D before = flight.getPosition();
        flight.move();
        Point2D after = flight.getPosition();

        assertNotEquals(before, after);
    }


    @Test
    void testIsArrivingTrue() {
        flight.setState(Flight.State.Arriving);
        assertTrue(flight.isArriving());
    }

    @Test
    void testIsArrivingFalse() {
        flight.setState(Flight.State.Departing);
        assertFalse(flight.isArriving());
    }


    @Test
    void testSetDestinationOnlyWhenNotArriving() {
        Point dest = new Point("teszt",new Point2D(200,200), Point.Type.dest);

        flight.setState(Flight.State.Departing);
        flight.setDestination(dest);

        assertEquals(dest, flight.getDestination());
    }


}