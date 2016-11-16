package com.mariebyleen.weather.location.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.CurrentLocationFinder;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment implements LocationViewContract {

    private LocationPresenterContract presenter;
    private ProgressDialog dialog;

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
        Context context = getContext();
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        presenter  = new LocationPresenter(this,
                new CurrentLocationFinder(new GoogleApiClient.Builder(getContext())
                        .addApi(LocationServices.API).build()));
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        presenter.useCurrentLocation();
    }

    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
        }
        dialog.setMessage(message);
        dialog.show();

    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
