package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

import javax.inject.Inject;

import butterknife.BindString;

public class LocationPresenter
        implements LocationPresenterContract {

    private final static String TAG = "LocationPresenter";

    @BindString(R.string.find_current_location_progress_dialog) String dialogText;

    private LocationViewContract view;
    private WeatherLocation location;

    @Inject
    public LocationPresenter(LocationViewContract view,
                             WeatherLocation location) {
        this.view = view;
        this.location = location;
    }

    @Override
    public void useCurrentLocation() {
        view.checkPermissions();
    }

    @Override
    public void onLocationPermissionGranted() {
        view.showProgressDialog(dialogText);
        location.updateLocationData();
        view.hideProgressDialog();
    }

    @Override
    public void onLocationPermissionDenied() {
        view.disableUseCurrentLocationOption();
    }

    @Override
    public void onStop() {

    }
}
