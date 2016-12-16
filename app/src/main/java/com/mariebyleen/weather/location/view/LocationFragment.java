package com.mariebyleen.weather.location.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.di.component.DaggerLocationComponent;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.presenter.LocationViewContract;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class LocationFragment extends Fragment implements LocationViewContract {

    private final static int PERMISSIONS_REQUEST_INTERNET = 0;
    private final static int PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION = 1;

    private ProgressDialog dialog;

    @Inject LocationPresenterContract presenter;

    @BindView(R.id.button_use_current_location)
    Button useCurrentLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        onCreateViewResolveDaggerDependency();
        return view;
    }

    private void onCreateViewResolveDaggerDependency() {
        DaggerLocationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .locationModule(new LocationModule(this, getContext()))
                .build().inject(this);
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

    public void disableUseCurrentLocationOption() {
        useCurrentLocation.setEnabled(false);
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onLocationPermissionGranted();
                } else {
                    presenter.onLocationPermissionDenied();
                }
                return;
            }
        }
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }
}
