package com.mariebyleen.weather.location.presenter;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

public class LocationPresenter implements LocationPresenterContract {

    private Context context;
    private GoogleApiAvailability availability;

    public LocationPresenter(Context context, GoogleApiAvailability availability) {
        this.context = context;
        this.availability = availability;
    }

    @Override
    public void useCurrentLocation() {
        if (isPlayServicesAvailableOnDevice()) {

        }
    }

    public boolean isPlayServicesAvailableOnDevice() {
        int status = availability.isGooglePlayServicesAvailable(context);
        if(status == ConnectionResult.SUCCESS)
            return true;
        else {
            Log.d("STATUS", "Google Play Services not available on this device. Code: "
                    + String.valueOf(status));
            return false;
        }
    }

}
