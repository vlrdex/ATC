package org.example.Model;

import javafx.geometry.Point2D;
import org.example.Controller.GameController;
import org.example.View.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javafx.util.Pair;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightSetHeadingTest {

    private Flight flight;
    private ACModel model;

    private GameController controller;
    private AirPort airport;
    private Runway runway;
    private Stats stats;
    private GameView gameView;

    @BeforeEach
    void setup() {
        model = new ACModel("teszt",900, 12000, 30, 60);

        flight = new Flight(
                90,
                250,
                2500,
                model,
                new Point2D(100, 100),
                Flight.State.WaitingForTakeOff
        );

        controller = mock(GameController.class);
        airport = mock(AirPort.class);
        runway = mock(Runway.class);
        stats = mock(Stats.class);
        gameView = mock(GameView.class);

        controller.airPort = airport;
        controller.stats = stats;
        controller.gameView = gameView;
    }



    @Test
    void setHeading_waitingForTakeOff_invalidPoint() {
        Point notRunway = new Point("teszt",new Point2D(50,50), Point.Type.dest);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(notRunway);

            verify(gameView).updateHistory("Invalid point for take off!");
            assertEquals(Flight.State.WaitingForTakeOff, flight.getState());
        }
    }

    @Test
    void setHeading_waitingForTakeOff_validRunway_setsStateToTakeOff() {
        Point runwayPoint = new Point("teszt1",new Point2D(0,0), Point.Type.runway);
        Point spawnPoint = new Point("teszt2",new Point2D(-50,0), Point.Type.runway);

        when(airport.getOppositeRunWayPoint(runwayPoint)).thenReturn(spawnPoint);
        when(airport.getRunwayWithPoint(runwayPoint)).thenReturn(runway);
        when(runway.getOccupied()).thenReturn(false);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(runwayPoint);

            assertEquals(Flight.State.TakeOff, flight.getState());
            verify(runway).setOccupied(true);
            assertEquals(spawnPoint.getPoint2D(), flight.getPosition());
        }
    }

    // ---------------- ARRIVING ----------------

    @Test
    void setHeading_arriving_wrongSpeedOrAltitude_doesNotLand() {
        flight.setState(Flight.State.Arriving);
        flight.setAssignedSpeed(200); // not 160
        flight.setAssignedAltitude(3000);

        Point runwayPoint = new Point("teszt",new Point2D(0,0), Point.Type.runway);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(runwayPoint);

            assertEquals(Flight.State.Arriving, flight.getState());
        }
    }

    @Test
    void setHeading_arriving_insideAssistZone_triggersLanding() {
        flight.setState(Flight.State.Arriving);
        flight.setAssignedSpeed(160);
        flight.setAssignedAltitude(1000);

        Point runwayPoint = new Point("teszt",new Point2D(0,0), Point.Type.runway);
        Point p1 = new Point("teszt2",new Point2D(50,50), Point.Type.near_assist);
        Point p2 = new Point("teszt3",new Point2D(200,200), Point.Type.far_assist);
        flight.setPosition(new Point2D(75,75));

        when(airport.getAssistPoints(runwayPoint)).thenReturn(new Pair<>(p1, p2));
        when(airport.getRunwayWithPoint(runwayPoint)).thenReturn(runway);
        when(runway.isWindWrong(any(), eq(runwayPoint))).thenReturn(false);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(runwayPoint);

            assertEquals(Flight.State.Landing, flight.getState());
        }
    }

    // ---------------- LANDING ----------------

    @Test
    void setHeading_landing_resetsToArriving() {
        flight.setState(Flight.State.Landing);

        Point p = new Point("teszt",new Point2D(10,10), Point.Type.far_assist);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(p);

            assertEquals(Flight.State.Arriving, flight.getState());
            assertEquals(p, flight.getHeading());
        }
    }

    // ---------------- DEFAULT ----------------

    @Test
    void setHeading_default_setsHeadingAndResetsDirection() {
        flight.setState(Flight.State.Departing);

        Point p = new Point("teszt",new Point2D(20,20), Point.Type.dest);

        try (MockedStatic<GameController> mocked = mockStatic(GameController.class)) {
            mocked.when(GameController::getInstance).thenReturn(controller);

            flight.setHeading(p);

            assertEquals(p, flight.getHeading());
        }
    }
}