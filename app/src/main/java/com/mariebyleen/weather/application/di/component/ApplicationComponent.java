package com.mariebyleen.weather.application.di.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.weather_display.job.WeatherJobCreator;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.weather_display.activity.MainActivity;
import com.mariebyleen.weather.weather_display.current_conditions.view.CurrentConditionsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context exposeContext();
    Resources exposeResources();
    SharedPreferences exposeSharedPreferences();
    Gson exposeGson();
    Navigator exposeNavigator();
    JobManager exposeJobManager();

    void inject(WeatherJobCreator weatherJobCreator);
    void inject(CurrentConditionsFragment currentConditionsFragment);
    void inject(MainActivity mainActivity);

}
