package com.mariebyleen.weather.location.model.fetcher;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.mariebyleen.weather.location.model.LocationFetcher;

import static android.content.ContentValues.TAG;

public class NetworkLocation implements LocationFetcher, LocationListener {

    private LocationManager locationManager;
    private Criteria criteria;

    private Location location;

    public NetworkLocation(LocationManager locationManager,
                           Criteria criteria) {
        this.locationManager = locationManager;
        this.criteria = criteria;
    }

    @Override
    public Location getLocation() {
        if (location == null)
            requestLocation();
        return location;
    }

    private void requestLocation() throws SecurityException {
        String providerName = locationManager.getBestProvider(criteria, false);
        if (providerName != null) {
            location = locationManager.getLastKnownLocation(providerName);
            if (location != null) {
                Log.d(TAG, "Network Provider... " + location.toString());
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }
}
