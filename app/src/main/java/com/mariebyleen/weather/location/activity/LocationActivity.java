package com.mariebyleen.weather.location.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.di.component.DaggerLocationComponent;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;
import com.mariebyleen.weather.navigation.Navigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class LocationActivity extends AppCompatActivity implements LocationViewContract {

    private final static int PERMISSIONS_REQUEST_ACCESS_COURSE_LOCATION = 1;

    @BindView(R.id.button_use_current_location)
    Button useCurrentLocation;
    @BindView(R.id.choose_location_field)
    AutoCompleteTextView locationTextView;
    @BindView(R.id.button_select_search_location)
    Button selectSearchLocation;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.button_select_recent_location)
    Button selectRecentLocation;

    @Inject
    LocationPresenter locationPresenter;
    @Inject
    Navigator navigator;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateResolveDaggerDependency();
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        onCreatePopulateSpinner();
    }

    private void onCreateResolveDaggerDependency() {
        DaggerLocationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .locationModule(new LocationModule(this, this))
                .build().inject(this);
    }

    private void onCreatePopulateSpinner() {
        String[] recentLocations = locationPresenter.getRecentLocationNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                recentLocations);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        locationPresenter.onViewResume();
        locationPresenter.setupSearchSuggestions(locationTextView);
        super.onResume();
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        locationPresenter.useCurrentLocation();
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
                    locationPresenter.onLocationPermissionGranted();
                } else {
                    locationPresenter.onLocationPermissionDenied();
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

    public void enableSearchLocationSelection(boolean enable) {
            selectSearchLocation.setEnabled(enable);
    }

    @Override
    public void enableUseRecentLocationSelection(boolean enable) {
        selectRecentLocation.setEnabled(enable);
    }

    @OnClick(R.id.button_select_search_location)
    public void selectSearchLocation() {
        locationPresenter.selectSearchLocation();
    }

    public String getSearchTextViewText() {
        return locationTextView.getText().toString();
    }

    public void navigateToMainActivity() {
        navigator.navigateToMain(this);
    }

    public void showNoAccessToWeatherServiceErrorMessage() {
        Toast.makeText(this, R.string.weather_service_not_available, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.button_select_recent_location)
    public void onClickSelectRecentLocation() {
        String selection = (String) spinner.getSelectedItem();
        locationPresenter.selectRecentLocation(selection);
    }

    @Override
    protected void onPause() {
        locationPresenter.onViewPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        locationPresenter.onViewStop();
        super.onStop();
    }
}
