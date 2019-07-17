package models.destinations;

import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTypeTest {

    @Test
    public void testEqual() {
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        assertEquals(destinationType1, destinationType2);
    }

    @Test
    public void testNotEqual() {
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("House");

        assertNotEquals(destinationType1, destinationType2);
    }

    @Test
    public void testEqualHashCode() {
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        assertEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }

    @Test
    public void testNotEqualHashCode() {
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();

        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("House");

        assertNotEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }
}