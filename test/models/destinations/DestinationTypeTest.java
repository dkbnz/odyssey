package models.destinations;

import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTypeTest {

    private static final String TYPE_STREAM = "Stream";
    private static final String TYPE_HOUSE = "House";


    @Test
    public void testEqual() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        assertEquals(destinationType1, destinationType2);
    }


    @Test
    public void testNotEqual() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_HOUSE);

        assertNotEquals(destinationType1, destinationType2);
    }


    @Test
    public void testEqualHashCode() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        assertEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }


    @Test
    public void testNotEqualHashCode() {
        Type destinationType1 = new Type();
        Type destinationType2 = new Type();

        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_HOUSE);

        assertNotEquals(destinationType1.hashCode(), destinationType2.hashCode());
    }
}