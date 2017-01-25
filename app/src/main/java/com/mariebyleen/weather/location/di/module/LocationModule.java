package com.mariebyleen.weather.location.di.module;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;

import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;
import com.mariebyleen.weather.location.presenter.RecentLocationsUpdater;
import com.mariebyleen.weather.location.presenter.SelectLocationMapper;
import com.mariebyleen.weather.location.recent_locations.database.Database;
import com.mariebyleen.weather.location.recent_locations.database.DatabaseReadWrite;
import com.mariebyleen.weather.location.recent_locations.database.RecentLocationsDbHelper;
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
    LocationPresenter provideLocationViewModel(LocationManager locationManager,
                                               Criteria criteria,
                                               GeoNamesApiService apiService,
                                               Preferences preferences,
                                               OpenWeatherCaller caller,
                                               Database database,
                                               RecentLocationsUpdater updater,
                                               SelectLocationMapper mapper) {
        return new LocationPresenter(view, criteria, locationManager, preferences, apiService, caller, database, updater, mapper);
    }

    @PerActivity
    @Provides
    RecentLocationsUpdater provideSelectLocationUtils() {
        return new RecentLocationsUpdater();
    }

    @PerActivity
    @Provides
    SelectLocationMapper provideSelectLocationMapper() {
        return new SelectLocationMapper();
    }

    @PerActivity
    @Provides
    Database provideDatabase(DatabaseReadWrite db) {
        return new Database(db);
    }

    @PerActivity
    @Provides
    DatabaseReadWrite provideDatabaseReadWrite(RecentLocationsDbHelper dbHelper) {
        return new DatabaseReadWrite(dbHelper);
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
}
