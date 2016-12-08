package com.mariebyleen.weather.location.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.model.service.FusedLocation;
import com.mariebyleen.weather.location.model.service.LocationFetcher;
import com.mariebyleen.weather.location.model.service.NetworkLocation;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

import dagger.Module;
import dagger.Provides;

@Module
@PerActivity
public class LocationModule {

    private LocationViewContract view;
    private Context context;

    public LocationModule(LocationViewContract view, Context context) {
        this.view = view;
        this.context = context;
    }

    @PerActivity
    @Provides
    LocationViewContract provideLocationViewContract() {
        return view;
    }

    @PerActivity
    @Provides
    LocationPresenterContract provideLocationPresenterContract(WeatherLocation location) {
        return new LocationPresenter(view, location);
    }

    @PerActivity
    @Provides
    WeatherLocation provideWeatherLocation(LocationFetcher locationFetcher,
                                           SharedPreferences preferences) {
        return new WeatherLocation(locationFetcher, preferences);
    }

    @PerActivity
    @Provides
    LocationFetcher provideLocationFetcher(GoogleApiAvailability availability,
                                           Context context,
                                           FusedLocation fusedLocation,
                                           NetworkLocation networkLocation) {
        int status = availability.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS)
            return fusedLocation;
        return networkLocation;
    }

    @PerActivity
    @Provides
    FusedLocation provideFusedLocation(GoogleApiClient client, Context context) {
        return new FusedLocation(client, context);
    }

    @PerActivity
    @Provides
    NetworkLocation provideNetworkLocation(LocationManager manager, Criteria criteria) {
        return new NetworkLocation(manager, criteria);
    }

    @PerActivity
    @Provides
    LocationManager provideLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @PerActivity
    @Provides
    Criteria provideCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setCostAllowed(false);
        return criteria;
    }

    @PerActivity
    @Provides
    GoogleApiAvailability provideGoogleApiAvailability() {
        return GoogleApiAvailability.getInstance();
    }

    @PerActivity
    @Provides
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

}
