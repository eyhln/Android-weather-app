package com.mariebyleen.weather.application.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.WeatherApplication;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.update_timer.AutomaticUpdateTimer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    GoogleApiClient exposeGoogleApiClient();
    GoogleApiAvailability exposeGoogleApiAvailability();
    Context exposeContext();
    OpenWeatherApiService exposeOpenWeatherApiService();
    SharedPreferences exposeSharedPreferences();
    Gson exposeGson();
    AutomaticUpdateTimer exposeAutomaticUpdateTimer();
    Navigator exposeNavigator();

    void inject(CurrentConditionsFragment currentConditionsFragment);
    void inject(WeatherApplication weatherApplication);
}
