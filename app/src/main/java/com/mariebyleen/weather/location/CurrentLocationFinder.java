package com.mariebyleen.weather.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;

public class CurrentLocationFinder implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private GoogleApiClient client;
    private Context context;
    private GoogleApiAvailability availability;

    @Inject
    public CurrentLocationFinder(GoogleApiClient client, Context context,
                                 GoogleApiAvailability availability) {
        this.client = client;
        this.context = context;
        this.availability = availability;
    }

    public Location getCurrentLocation() {
        if (isPlayServicesAvailableOnDevice()) {

        }
        return null;
    }

    public boolean isPlayServicesAvailableOnDevice() {
        int status = availability.isGooglePlayServicesAvailable(context);
        return (status == ConnectionResult.SUCCESS);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
