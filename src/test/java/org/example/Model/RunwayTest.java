package org.example.Model;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RunwayTest {

    private Runway createRunway() {
        Point p1 = new Point("R1", new Point2D(0,0), Point.Type.runway);
        Point p2 = new Point("R2", new Point2D(100,0), Point.Type.runway);

        return new Runway(List.of(p1,p2));
    }

    @Test
    void constructorShouldStoreEndpoints() {
        Runway runway = createRunway();

        assertEquals(2, runway.getEndPoints().size());
    }

    @Test
    void occupiedSetterGetterShouldWork() {
        Runway runway = createRunway();

        runway.setOccupied(true);

        assertTrue(runway.getOccupied());
    }

    @Test
    void dirVectorSetterGetterShouldWork() {
        Runway runway = createRunway();

        Point2D vec = new Point2D(1,0);
        runway.setDirVector(vec);

        assertEquals(vec, runway.getDirVector());
    }

    @Test
    void isWindWrongShouldReturnBooleanWithoutCrash() {
        Runway runway = createRunway();

        Wind wind = new Wind(0,10);

        Point p = runway.getEndPoints().get(0);

        boolean result = runway.isWindWrong(wind,p);

        assertNotNull(result);
    }

}