package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.CurrentLocationFinder;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

import butterknife.BindString;

public class LocationPresenter implements LocationPresenterContract {

    @BindString(R.string.find_current_location_progress_dialog) String dialogText;
    private LocationViewContract view;
    private CurrentLocationFinder currentLocationFinder;

    public LocationPresenter(LocationViewContract view,
                             CurrentLocationFinder currentLocationFinder) {
        this.view = view;
        this.currentLocationFinder = currentLocationFinder;
    }

    @Override
    public void useCurrentLocation() {
        view.showProgressDialog(dialogText);
        currentLocationFinder.getCurrentLocation();
        view.hideProgressDialog();
    }

}
