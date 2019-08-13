package models.destinations;

import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTypeTest {

    @Test
    public void testEqual() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        assertEquals(destinationType1, destinationType2);
    }

    @Test
    public void testNotEqual() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("House");

        assertNotEquals(destinationType1, destinationType2);
    }

    @Test
    public void testEqualHashCode() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        assertEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }

    @Test
    public void testNotEqualHashCode() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("House");

        assertNotEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }
}