package com.mariebyleen.weather.location.model;


import android.location.Location;

import com.mariebyleen.weather.location.model.service.LocationFetcher;

import javax.inject.Inject;

public class WeatherLocation {

    private Location location;

    private LocationFetcher locationFetcher;

    @Inject
    public WeatherLocation(LocationFetcher locationFetcher) {
      this.locationFetcher = locationFetcher;
    }

    public Location updateLocationData() {
        if (location == null) {
            saveLocation(locationFetcher.getLocation());
        }
        return location;
    }

    private void saveLocation(Location location) {}

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
