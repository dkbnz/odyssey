package models.destinations;

import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTest {

    @Test
    public void testEqual() {
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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
        DestinationType destinationType1 = new DestinationType();
        DestinationType destinationType2 = new DestinationType();
        Destination testDestination1 = new Destination();
        Destination testDestination2 = new Destination();

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