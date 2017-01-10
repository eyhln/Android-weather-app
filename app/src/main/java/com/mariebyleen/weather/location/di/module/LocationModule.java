package com.mariebyleen.weather.location.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.location.model.LocationFetcher;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.model.fetcher.FusedLocation;
import com.mariebyleen.weather.location.model.fetcher.NetworkLocation;
import com.mariebyleen.weather.location.recent_locations.database.Database;
import com.mariebyleen.weather.location.recent_locations.database.RecentLocationsDbHelper;
import com.mariebyleen.weather.location.view_model.LocationViewContract;
import com.mariebyleen.weather.location.view_model.LocationPresenter;
import com.mariebyleen.weather.preferences.Preferences;

import dagger.Module;
import dagger.Provides;

@Module
@PerActivity
public class LocationModule {

    private Context context;
    private LocationViewContract view;

    public LocationModule(LocationViewContract view, Context context) {
        this.view = view;
        this.context = context;
    }

    @PerActivity
    @Provides
    LocationPresenter provideLocationViewModel(WeatherLocation location,
                                               GeoNamesApiService apiService,
                                               Preferences preferences,
                                               OpenWeatherCaller caller,
                                               Database database) {
        return new LocationPresenter(view, location, apiService, preferences, caller, database);
    }

    @PerActivity
    @Provides
    Database provideDatabase(RecentLocationsDbHelper dbHelper) {
        return new Database(dbHelper);
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
