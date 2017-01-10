package com.mariebyleen.weather.application.di.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.job.WeatherJobCreator;
import com.mariebyleen.weather.location.recent_locations.database.RecentLocationsDbHelper;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.preferences.Preferences;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context exposeContext();
    Preferences exposePreferences();
    SharedPreferences exposeSharedPreferences();
    OpenWeatherCaller exposeOpenWeatherCaller();
    Resources exposeResources();
    Navigator exposeNavigator();
    JobManager exposeJobManager();
    GeoNamesApiService exposeGeoNamesApiService();
    RecentLocationsDbHelper exposeRecentLocationsDbHelper();

    void inject(WeatherJobCreator weatherJobCreator);
}
