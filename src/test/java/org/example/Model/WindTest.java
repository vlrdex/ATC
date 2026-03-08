package org.example.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindTest {

    @Test
    void constructorShouldNormalizeDirectionAbove360() {
        Wind wind = new Wind(370, 10);

        assertEquals(10, wind.getDirection(), 0.0001);
    }

    @Test
    void constructorShouldNormalizeNegativeDirection() {
        Wind wind = new Wind(-30, 10);

        assertEquals(330, wind.getDirection(), 0.0001);
    }

    @Test
    void gettersShouldReturnInitialValues() {
        Wind wind = new Wind(90, 15);

        assertEquals(90, wind.getDirection(), 0.0001);
        assertEquals(15, wind.getSpeed(), 0.0001);
    }

    @Test
    void changeDirectionShouldKeepDirectionWithinRange() {
        Wind wind = new Wind(180, 10);

        for (int i = 0; i < 10000; i++) {
            wind.changeDirection();
            assertTrue(wind.getDirection() >= 0);
            assertTrue(wind.getDirection() < 360);
        }
    }

    @Test
    void changeSpeedShouldStayWithinBounds() {
        Wind wind = new Wind(180, 30);

        for (int i = 0; i < 10000; i++) {
            wind.changeSpeed();
            assertTrue(wind.getSpeed() >= 0);
            assertTrue(wind.getSpeed() <= 60);
        }
    }

    @Test
    void changeShouldModifyWindButStayWithinValidRanges() {
        Wind wind = new Wind(180, 20);

        for (int i = 0; i < 10000; i++) {
            wind.change();

            assertTrue(wind.getDirection() >= 0);
            assertTrue(wind.getDirection() < 360);

            assertTrue(wind.getSpeed() >= 0);
            assertTrue(wind.getSpeed() <= 60);
        }
    }

    @Test
    void toStringShouldContainWindInformation() {
        Wind wind = new Wind(100, 25);

        String result = wind.toString();

        assertTrue(result.contains("Wind"));
        assertTrue(result.contains("direction"));
        assertTrue(result.contains("speed"));
    }
}