package com.mariebyleen.weather.location.model;


import android.content.SharedPreferences;
import android.location.Location;

import javax.inject.Inject;

public class UserLocation {

    private LocationFetcher locationFetcher;
    private SharedPreferences preferences;

    private Location location;

    @Inject
    public UserLocation(LocationFetcher locationFetcher,
                        SharedPreferences preferences) {
        this.locationFetcher = locationFetcher;
        this.preferences = preferences;
    }

    public Location getLocation() {
        return location;
    }

    public void updateLocationData() {
        this.location = locationFetcher.getLocation();
        saveLocationCoordinates(location);
    }

    private void saveLocationCoordinates(Location location) {
        if (location != null) {
            float lat = (float) location.getLatitude();
            float lon = (float) location.getLongitude();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("lat", lat);
            editor.putFloat("lon", lon);
            editor.apply();
        }
    }
}
