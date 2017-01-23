package com.mariebyleen.weather.location.recent_locations.model;

public class RecentLocation {

    private String name;
    private float lat;
    private float lon;
    private long time;

    public RecentLocation(String name, float lat, float lon, long time) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
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

    public long getTime() { return time; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecentLocation that = (RecentLocation) o;

        if (Float.compare(that.lat, lat) != 0) return false;
        if (Float.compare(that.lon, lon) != 0) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (lat != +0.0f ? Float.floatToIntBits(lat) : 0);
        result = 31 * result + (lon != +0.0f ? Float.floatToIntBits(lon) : 0);
        return result;
    }
}
