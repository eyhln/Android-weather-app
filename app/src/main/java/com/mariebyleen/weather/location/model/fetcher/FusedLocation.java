package com.mariebyleen.weather.location.model.fetcher;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mariebyleen.weather.location.model.LocationFetcher;

public class FusedLocation implements LocationFetcher,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient googleApiClient;
    private Context context;

    private Location location;

    public FusedLocation(GoogleApiClient googleApiClient,
                         Context context) {
        this.googleApiClient = googleApiClient;
        this.context = context;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    private void requestLocationUsingFusedLocationProvider() {
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) throws SecurityException {
        android.location.Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);
        if (lastLocation != null)
            location = lastLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void closeConnections() {
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

}
