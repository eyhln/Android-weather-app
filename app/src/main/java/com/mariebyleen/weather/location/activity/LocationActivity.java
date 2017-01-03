package com.mariebyleen.weather.location.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.location.di.component.DaggerLocationComponent;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.view_model.LocationViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class LocationActivity extends BaseActivity implements LocationViewContract {

    private final static int PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION = 1;

    private ProgressDialog dialog;

    @BindView(R.id.button_use_current_location)
    Button useCurrentLocation;

    @Inject
    LocationViewModel viewModel;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        onCreateResolveDaggerDependency();
    }

    private void onCreateResolveDaggerDependency() {
        DaggerLocationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .locationModule(new LocationModule(this, this))
                .build().inject(this);
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        viewModel.useCurrentLocation();
    }

    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
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
                    viewModel.onLocationPermissionGranted();
                } else {
                    viewModel.onLocationPermissionDenied();
                }
                return;
            }
        }
    }

    @Override
    public void onStop() {
        viewModel.onStop();
        super.onStop();
    }
}
