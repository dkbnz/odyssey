package models;

public class Destination {
    private String destination;
    private String type;
    private String district;
    private double latitude;
    private double longitude;
    private String country;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Destination(String destination, String type, String district, double latitude, double longitude, String country) {
        this.destination = destination;
        this.type = type;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
}
