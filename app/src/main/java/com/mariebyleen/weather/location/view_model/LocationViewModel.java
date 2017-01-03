package com.mariebyleen.weather.location.view_model;

import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.activity.LocationViewContract;

import javax.inject.Inject;

public class LocationViewModel {

    private final String dialogText = "";

    private WeatherLocation location;
    private LocationViewContract view;

    @Inject
    public LocationViewModel(LocationViewContract view, WeatherLocation location) {
        this.location = location;
    }

    public void useCurrentLocation() {
        view.checkPermissions();
    }

    public void onLocationPermissionGranted() {
        view.showProgressDialog(dialogText);
        location.updateLocationData();
        view.hideProgressDialog();
    }

    public void onLocationPermissionDenied() {
        view.disableUseCurrentLocationOption();
    }

    public void onStop() {

    }
}
