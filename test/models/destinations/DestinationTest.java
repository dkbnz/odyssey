package models.destinations;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTest {

    private Type destinationType1;
    private Type destinationType2;
    private Destination testDestination1;
    private Destination testDestination2;

    @Before
    public void setUp() {
        destinationType1 = new Type();
        destinationType2 = new Type();
        testDestination1 = new Destination();
        testDestination2 = new Destination();
    }

    @Test
    public void testEqual() {
        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("River of the Great");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("River of the Great");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertEquals(testDestination1, testDestination2);
    }

    @Test
    public void testNameNotEqual() {
        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("River of the Great"); // Name not equal therefore Destination not equal
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1, testDestination2);
    }

    @Test
    public void testTypeNotEqual() {
        // DestinationType not equal therefore Destination not equal, Ceteris paribus
        destinationType1.setDestinationType("River");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("Big Stream");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1, testDestination2);
    }

    @Test
    public void testLatitudeNotEqual() {
        destinationType1.setDestinationType("River");
        destinationType2.setDestinationType("River");

        testDestination1.setName("Big Stream");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.4244); // Latitude not equal therefore Destination not equal
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1, testDestination2);
    }


    @Test
    public void testEqualHashCode() {
        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("River of the Great");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("River of the Great");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testNameNotEqualHashCode() {
        destinationType1.setDestinationType("Stream");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("River of the Great"); // Name not equal therefore Destination not equal
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testTypeNotEqualHashCode() {
        // DestinationType not equal therefore Destination not equal, Ceteris paribus
        destinationType1.setDestinationType("River");
        destinationType2.setDestinationType("Stream");

        testDestination1.setName("Big Stream");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.42344);
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testLatitudeNotEqualHashCode() {
        destinationType1.setDestinationType("River");
        destinationType2.setDestinationType("River");

        testDestination1.setName("Big Stream");
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict("FakeVille");
        testDestination1.setCountry("Flogoria");
        testDestination1.setLatitude(32.4244); // Latitude not equal therefore Destination not equal
        testDestination1.setLongitude(34.43643);

        testDestination2.setName("Big Stream");
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict("FakeVille");
        testDestination2.setCountry("Flogoria");
        testDestination2.setLatitude(32.42344);
        testDestination2.setLongitude(34.43643);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }
}