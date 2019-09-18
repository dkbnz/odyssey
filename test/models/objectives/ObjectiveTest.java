package models.objectives;

import models.destinations.Destination;
import models.profiles.Profile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ObjectiveTest {

    private Objective testObjective1;
    private Objective testObjective2;
    private Destination destination1;
    private Profile objectiveOwner1;
    private Profile objectiveOwner2;

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
                -> error.getMessage().equals("A riddle must be provided.")));
    }


    @Test
    public void getErrorsRiddleEmpty() {
        testObjective1.setRiddle("");
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("A riddle must be provided.")));
    }


    @Test
    public void getErrorsRiddleMaxValue() {
        // Generates a string of length 256.
        testObjective1.setRiddle(new String(new char[256]).replace("\0", "S"));
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("Objective riddles must not exceed 255 characters in length.")));
    }


    @Test
    public void getErrorsRiddleBoundaryMaxValue() {
        // Generates a string of length 255.
        testObjective1.setRiddle(new String(new char[255]).replace("\0", "S"));
        assertFalse(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("Objective riddles must not exceed 255 characters in length.")));
    }


    @Test
    public void getErrorsOwnerNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("This objective does not have an owner.")));
    }


    @Test
    public void getErrorsDestinationNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("Objectives must have a destination.")));
    }


    @Test
    public void getErrorsRadiusNull() {
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("You must select a range for an objective destination's check in")));
    }


    @Test
    public void getErrorsRadiusMinValue() {
        testObjective1.setRadius(0.0);
        assertTrue(testObjective1.getErrors().stream().anyMatch(error
                -> error.getMessage().equals("You must select a range for an objective destination's check in")));
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
        testObjective1.setRiddle("Stream");
        testObjective2.setRiddle("Stream");

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
        testObjective1.setRiddle("Stream by the lake");
        testObjective2.setRiddle("Stream by the sea"); // Riddle not equal, therefore objective not equal.

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
        testObjective1.setRiddle("Stream by the sea");
        testObjective2.setRiddle("Stream by the sea");

        testObjective1.setRadius(0.5);
        testObjective2.setRadius(0.1); // Radius not equal, therefore objective not equal.

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1, testObjective2);
    }


    @Test
    public void testOwnerNotEqual() {
        testObjective1.setRiddle("Stream by the sea");
        testObjective2.setRiddle("Stream by the sea");

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner2); // Owner not equal, therefore objective not equal.

        assertNotEquals(testObjective1, testObjective2);
    }


    @Test
    public void testEqualHashCode() {
        testObjective1.setRiddle("Stream");
        testObjective2.setRiddle("Stream");

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
        testObjective1.setRiddle("Stream by the lake");
        testObjective2.setRiddle("Stream by the sea"); // Riddle not equal, therefore objective not equal.

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
        testObjective1.setRiddle("Stream by the sea");
        testObjective2.setRiddle("Stream by the sea");

        testObjective1.setRadius(0.5);
        testObjective2.setRadius(0.1); // Radius not equal, therefore objective not equal.

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner1);

        assertNotEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }


    @Test
    public void testOwnerNotEqualHashCode() {
        testObjective1.setRiddle("Stream by the sea");
        testObjective2.setRiddle("Stream by the sea");

        testObjective1.setRadius(0.1);
        testObjective2.setRadius(0.1);

        testObjective1.setDestination(destination1);
        testObjective2.setDestination(destination1);

        testObjective1.setOwner(objectiveOwner1);
        testObjective2.setOwner(objectiveOwner2); // Owner not equal, therefore objective not equal.

        assertNotEquals(testObjective1.hashCode(), testObjective2.hashCode());
    }
}
