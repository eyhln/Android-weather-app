package com.mariebyleen.weather.location.recent_locations.model;

public class RecentLocation {

    private String name;
    private float lat;
    private float lon;

    public RecentLocation(String name, float lat, float lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }
}
