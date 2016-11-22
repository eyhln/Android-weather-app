package com.mariebyleen.weather.location.model;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import static android.content.ContentValues.TAG;

public class CurrentLocation implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private Location currentLocation;

    private GoogleApiClient googleApiClient;
    private Context context;
    private GoogleApiAvailability availability;
    private LocationManager locationManager;
    private Criteria criteria;

    @Inject
    public CurrentLocation(GoogleApiClient googleApiClient, Context context,
                           GoogleApiAvailability availability, LocationManager locationManager,
                           Criteria criteria) {
        this.googleApiClient = googleApiClient;
        this.context = context;
        this.availability = availability;
        this.locationManager = locationManager;
        this.criteria = criteria;
    }

    public Location getLocation() {
        if (currentLocation == null)
            requestLastLocation();
        return currentLocation;
    }

    private void requestLastLocation() {
        if (isPlayServicesAvailableOnDevice()) {
            requestLocationUsingFusedLocationProvider();
        }
        else {
            requestLocationUsingNetworkProvider();
        }

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
            boolean permissionGranted = checkCourseLocationPermission();
            if (!permissionGranted) {
                //requestPermission();
            }
            else {
                Location lastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(googleApiClient);
                if (lastLocation != null)
                    currentLocation = lastLocation;
            }
        }

            private boolean checkCourseLocationPermission() {
                return (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);
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
        boolean permissionGranted = checkCourseLocationPermission();
        if (!permissionGranted) {
            // request permissions
        }
        else {
            String providerName = locationManager.getBestProvider(criteria, false);
            currentLocation = locationManager.getLastKnownLocation(providerName);
            Log.d(TAG, "Network Provider... " + currentLocation.toString());
        }
    }


    public void closeConnections() {
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }


}
