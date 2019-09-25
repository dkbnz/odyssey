package models.destinations;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DestinationTest {

    private Type destinationType1;
    private Type destinationType2;
    private Destination testDestination1;
    private Destination testDestination2;

    private static final String TYPE_STREAM = "Stream";
    private static final String TYPE_RIVER = "River";
    private static final String NAME_RIVER = "River of the Great";
    private static final String NAME_STREAM = "Big Stream";
    private static final String DISTRICT_FAKE_VILLE = "FakeVille";
    private static final String COUNTRY_FLOGORIA = "Flogoria";
    private static final Double LATITUDE_1 = 32.42344;
    private static final Double LATITUDE_2 = 32.4244;
    private static final Double LONGITUDE_1 = 34.43643;

    @Before
    public void setUp() {
        destinationType1 = new Type();
        destinationType2 = new Type();
        testDestination1 = new Destination();
        testDestination2 = new Destination();
    }

    @Test
    public void testEqual() {
        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        testDestination1.setName(NAME_RIVER);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_RIVER);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertEquals(testDestination1, testDestination2);
    }

    @Test
    public void testNameNotEqual() {
        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        // Name not equal therefore Destination not equal
        testDestination1.setName(NAME_RIVER);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1, testDestination2);
    }

    @Test
    public void testTypeNotEqual() {
        // DestinationType not equal therefore Destination not equal, Ceteris paribus
        destinationType1.setDestinationType(TYPE_RIVER);
        destinationType2.setDestinationType(TYPE_STREAM);

        testDestination1.setName(NAME_STREAM);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1, testDestination2);
    }

    @Test
    public void testLatitudeNotEqual() {
        destinationType1.setDestinationType(TYPE_RIVER);
        destinationType2.setDestinationType(TYPE_RIVER);

        testDestination1.setName(NAME_STREAM);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        // Latitude not equal therefore Destination not equal
        testDestination1.setLatitude(LATITUDE_2);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1, testDestination2);
    }


    @Test
    public void testEqualHashCode() {
        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        testDestination1.setName(NAME_RIVER);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_RIVER);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testNameNotEqualHashCode() {
        destinationType1.setDestinationType(TYPE_STREAM);
        destinationType2.setDestinationType(TYPE_STREAM);

        // Name not equal therefore Destination not equal
        testDestination1.setName(NAME_RIVER);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testTypeNotEqualHashCode() {
        // DestinationType not equal therefore Destination not equal, Ceteris paribus
        destinationType1.setDestinationType(TYPE_RIVER);
        destinationType2.setDestinationType(TYPE_STREAM);

        testDestination1.setName(NAME_STREAM);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        testDestination1.setLatitude(LATITUDE_1);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }

    @Test
    public void testLatitudeNotEqualHashCode() {
        destinationType1.setDestinationType(TYPE_RIVER);
        destinationType2.setDestinationType(TYPE_RIVER);

        testDestination1.setName(NAME_STREAM);
        testDestination1.setType(destinationType1);
        testDestination1.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination1.setCountry(COUNTRY_FLOGORIA);
        // Latitude not equal therefore Destination not equal
        testDestination1.setLatitude(LATITUDE_2);
        testDestination1.setLongitude(LONGITUDE_1);

        testDestination2.setName(NAME_STREAM);
        testDestination2.setType(destinationType2);
        testDestination2.setDistrict(DISTRICT_FAKE_VILLE);
        testDestination2.setCountry(COUNTRY_FLOGORIA);
        testDestination2.setLatitude(LATITUDE_1);
        testDestination2.setLongitude(LONGITUDE_1);

        assertNotEquals(testDestination1.hashCode(), testDestination2.hashCode());
    }
}