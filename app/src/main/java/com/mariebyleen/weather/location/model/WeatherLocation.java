package com.mariebyleen.weather.location.model;


import android.content.SharedPreferences;
import android.location.Location;

import com.mariebyleen.weather.location.model.service.LocationFetcher;

import javax.inject.Inject;

public class WeatherLocation {

    private Location location;

    private LocationFetcher locationFetcher;
    private SharedPreferences preferences;

    @Inject
    public WeatherLocation(LocationFetcher locationFetcher,
                           SharedPreferences preferences) {
      this.locationFetcher = locationFetcher;
      this.preferences = preferences;
    }

    public Location getLocation() {
        return location;
    }

    public void updateLocationData() {
        checkPermissions();
        saveLocationCoordinates(locationFetcher.getLocation());
    }

    private void saveLocationCoordinates(Location location) {
        float lat = (float)location.getLatitude();
        float lon = (float)location.getLongitude();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("lat", lat);
        editor.putFloat("lon", lon);
        editor.apply();
    }

    public void checkPermissions() {
        boolean permissionGranted = checkCourseLocationPermission();
        if (!permissionGranted) {
            // request permissions
        }
    }

    private boolean checkCourseLocationPermission() {
        return false;
    }








}
