package com.mariebyleen.weather.location.model;


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

public class CurrentLocation implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private Location currentLocation;

    private GoogleApiClient googleApiClient;
    private Context context;
    private GoogleApiAvailability availability;

    @Inject
    public CurrentLocation(GoogleApiClient googleApiClient, Context context,
                           GoogleApiAvailability availability) {
        this.googleApiClient = googleApiClient;
        this.context = context;
        this.availability = availability;

    }

    public Location getCurrentLocation() {
        if (currentLocation == null)
            requestCurrentLocation();
        return currentLocation;
    }

    private void requestCurrentLocation() {
        if (isPlayServicesAvailableOnDevice())
            requestLocationUsingFusedLocationProvider();
        else
            requestLocationUsingNetworkProvider();

    }

    public boolean isPlayServicesAvailableOnDevice() {
        int status = availability.isGooglePlayServicesAvailable(context);
        return (status == ConnectionResult.SUCCESS);
    }

    private void requestLocationUsingFusedLocationProvider() {
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();
    }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            /**
            Location lastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient);
            if (lastLocation != null)
                currentLocation = lastLocation;
             */
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

    private void requestLocationUsingNetworkProvider() {

    }

    public void closeConnections() {
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }
}
