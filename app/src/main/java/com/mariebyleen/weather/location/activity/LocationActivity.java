package com.mariebyleen.weather.location.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.databinding.ActivityLocationBinding;
import com.mariebyleen.weather.location.di.component.DaggerLocationComponent;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.view_model.LocationViewContract;
import com.mariebyleen.weather.location.view_model.LocationViewModel;
import com.mariebyleen.weather.navigation.Navigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class LocationActivity extends BaseActivity implements LocationViewContract {

    private final static int PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION = 1;

    @BindView(R.id.button_use_current_location)
    Button useCurrentLocation;
    @BindView(R.id.choose_location_field)
    AutoCompleteTextView locationTextView;
    @BindView(R.id.button_select_location)
    Button selectLocation;

    @Inject
    LocationViewModel viewModel;
    @Inject
    Navigator navigator;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateResolveDaggerDependency();
        onCreateSetupDataBinding();
        ButterKnife.bind(this);
    }

    private void onCreateResolveDaggerDependency() {
        DaggerLocationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .locationModule(new LocationModule(this, this))
                .build().inject(this);
    }

    private void onCreateSetupDataBinding() {
        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onResume() {
        viewModel.setupSearchSuggestions(locationTextView, this, selectLocation);
        super.onResume();
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        viewModel.useCurrentLocation();
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

    public void showLocationSuggestions(String[] suggestions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions);
        locationTextView.setAdapter(adapter);
        locationTextView.showDropDown();
    }

    @OnClick(R.id.button_select_location)
    public void selectSearchLocation() {
        viewModel.selectSearchLocation();
    }

    public String getSearchTextViewText() {
        return locationTextView.getText().toString();
    }

    public void navigateToMainActivity() {
        navigator.navigateToMain(this);
    }

    @Override
    public void onStop() {
        viewModel.onViewStop();
        super.onStop();
    }
}
