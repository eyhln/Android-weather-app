package com.mariebyleen.weather.weather_display.model.current_conditions;

public class CurrentConditionsResponseCoord {
    private double lon;
    private double lat;

    public double getLon() {
        return this.lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }
}
