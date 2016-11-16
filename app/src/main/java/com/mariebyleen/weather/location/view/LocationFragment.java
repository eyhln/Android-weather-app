package com.mariebyleen.weather.location.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment implements LocationViewContract {

    private final static int REQUEST_CODE_GOOGLE_SERVICES_UPDATE = 0;

    private LocationPresenterContract presenterContract;
    private Context context;
    private GoogleApiAvailability availability;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        onCreateViewCreateDependencies();
        return view;
    }

    private void onCreateViewCreateDependencies() {
        context = getContext();
        availability = GoogleApiAvailability.getInstance();
        presenterContract = new LocationPresenter(context, availability, this);
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        presenterContract.useCurrentLocation();
    }
}
