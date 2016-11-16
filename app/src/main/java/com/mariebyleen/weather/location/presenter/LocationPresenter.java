package com.mariebyleen.weather.location.presenter;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

public class LocationPresenter implements LocationPresenterContract {

    private LocationViewContract view;
    private Context context;
    private GoogleApiAvailability availability;

    public LocationPresenter(Context context, GoogleApiAvailability availability,
                             LocationViewContract view) {
        this.context = context;
        this.availability = availability;
        this.view = view;
    }

    @Override
    public void useCurrentLocation() {
        if (isPlayServicesAvailableOnDevice()) {

        }
    }

    public boolean isPlayServicesAvailableOnDevice() {
        int status = availability.isGooglePlayServicesAvailable(context);
        return (status == ConnectionResult.SUCCESS);
    }

}
