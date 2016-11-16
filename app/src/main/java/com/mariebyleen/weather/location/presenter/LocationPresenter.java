package com.mariebyleen.weather.location.presenter;

import android.util.Log;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.CurrentLocationFinder;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

import javax.inject.Inject;

import butterknife.BindString;

public class LocationPresenter
        implements LocationPresenterContract {

    private final static String TAG = "LocationPresenter";

    @BindString(R.string.find_current_location_progress_dialog) String dialogText;

    private LocationViewContract view;
    private CurrentLocationFinder currentLocationFinder;

    @Inject
    public LocationPresenter(LocationViewContract view,
                             CurrentLocationFinder currentLocationFinder) {
        this.view = view;
        this.currentLocationFinder = currentLocationFinder;
    }

    @Override
    public void useCurrentLocation() {
        view.showProgressDialog(dialogText);
        Log.d(TAG, "using current location");
        currentLocationFinder.getCurrentLocation();
        view.hideProgressDialog();
    }

}
