package com.mariebyleen.weather.current_conditions.model;

public class CurrentConditionsResponseCoord {
    private int lon;
    private int lat;

    public int getLon() {
        return this.lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return this.lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }
}
