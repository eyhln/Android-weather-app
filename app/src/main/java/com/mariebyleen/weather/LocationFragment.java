package com.mariebyleen.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment {

    private LocationContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        if (playServicesAvailableOnDevice()) {

        }
    }


    private boolean playServicesAvailableOnDevice() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int status = availability.isGooglePlayServicesAvailable(getContext());
        if(status == ConnectionResult.SUCCESS)
            return true;
        else {
            Log.d("STATUS", "Google Play Services not available on this device. Code: "
                    + String.valueOf(status));
            return false;
        }
    }







}
