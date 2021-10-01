package ru.v0b0.geocoder.entities;

public class Point {

    private String request;

    private String address;

    private String coordinates;

    public Point() {}

    public Point(String request) {
        this.request = request;
    }

    public Point(String address, String coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
