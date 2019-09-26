package models.objectives;

import models.destinations.Destination;
import models.profiles.Profile;
import models.util.Errors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ObjectiveTest {

    private Objective testObjective1;
    private Objective testObjective2;
    private Destination destination1;
    private Profile objectiveOwner1;
    private Profile objectiveOwner2;

    private static final String RIDDLE_STREAM = "Stream";
    private static final String RIDDLE_STREAM_LAKE = "Stream by the lake";
    private static final String RIDDLE_STREAM_SEA = "Stream by the sea";


    @Before
    public void setUp() {
        testObjective1 = new Objective();
        testObjective2 = new Objective();
        destination1 = new Destination();
        objectiveOwner1 = new Profile();
        objectiveOwner2 = new Profile();
    }


    @Test
    public void getErrorsRiddleNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_RIDDLE.toString())));
    }


    @Test
    public void getErrorsRiddleEmpty() {
        testObjective1.setRiddle("");
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_RIDDLE.toString())));
    }


    @Test
    public void getErrorsRiddleMaxValue() {
        // Generates a string of length 256.
        testObjective1.setRiddle(new String(new char[256]).replace("\0", "S"));
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.MAX_RIDDLE_LENGTH.toString())));
    }


    @Test
    public void getErrorsRiddleBoundaryMaxValue() {
        // Generates a string of length 255.
        testObjective1.setRiddle(new String(new char[255]).replace("\0", "S"));
        assertFalse(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.MAX_RIDDLE_LENGTH.toString())));
    }


    @Test
    public void getErrorsOwnerNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_OWNER.toString())));
    }


    @Test
    public void getErrorsDestinationNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_DESTINATION.toString())));
    }


    @Test
    public void getErrorsRadiusNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_RADIUS.toString())));
    }


    @Test
    public void getErrorsRadiusMinValue() {
        testObjective1.setRadius(0.0);
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals(Errors.NO_OBJECTIVE_RADIUS.toString())));
    }


    @Test
    public void getSetDestination() {
        testObjective1.setDestination(destination1);
        assertEquals(destination1, testObjective1.getDestination());
    }


    @Test
    public void getSetRiddle() {
        testObjective1.setRiddle("I am a riddle.");
        assertEquals("I am a riddle.", testObjective1.getRiddle());
    }


    @Test
    public void getSetOwner() {
        testObjective1.setOwner(objectiveOwner1);
        assertEquals(objectiveOwner1, testObjective1.getOwner());
    }


    @Test
    public void getSetRadius() {
        testObjective1.setRadius(0.05);
        assertEquals(0.05, testObjective1.getRadius(), 0);
    }


    @Test
    public void testEqual() {
        testObjective1.setRiddle(RIDDLE_STREAM);
        testObjective2.setRiddle(RIDDLE_STREAM);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertEquals(testObjective1, testObjective2);
    }


    @Test
    public void testRiddleNotEqual() {
        // Riddle not equal, therefore objective not equal.
        testObjective1.setRiddle(RIDDLE_STREAM_LAKE);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1, testObjective2);
    }


    @Test
    public void testRadiusNotEqual() {
        testObjective1.setRiddle(RIDDLE_STREAM_SEA);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        // Radius not equal, therefore objective not equal.
        testObjective1.setRadius(0.5);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1, testObjective2);
    }


    @Test
    public void testOwnerNotEqual() {
        testObjective1.setRiddle(RIDDLE_STREAM_SEA);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        // Owner not equal, therefore objective not equal.
        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner2);

        assertNotEquals(testObjective1, testObjective2);
    }


    @Test
    public void testEqualHashCode() {
        testObjective1.setRiddle(RIDDLE_STREAM);
        testObjective2.setRiddle(RIDDLE_STREAM);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }


    @Test
    public void testRiddleNotEqualHashCode() {
        // Riddle not equal, therefore objective not equal.
        testObjective1.setRiddle(RIDDLE_STREAM_LAKE);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }


    @Test
    public void testRadiusNotEqualHashCode() {
        testObjective1.setRiddle(RIDDLE_STREAM_SEA);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        // Radius not equal, therefore objective not equal.
        testObjective1.setRadius(0.5);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }


    @Test
    public void testOwnerNotEqualHashCode() {
        testObjective1.setRiddle(RIDDLE_STREAM_SEA);
        testObjective2.setRiddle(RIDDLE_STREAM_SEA);

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        // Owner not equal, therefore objective not equal.
        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner2);

        assertNotEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }
}
