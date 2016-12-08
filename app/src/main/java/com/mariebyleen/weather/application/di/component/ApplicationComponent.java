package com.mariebyleen.weather.application.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;
import com.mariebyleen.weather.job.WeatherJobCreator;
import com.mariebyleen.weather.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context exposeContext();
    SharedPreferences exposeSharedPreferences();
    Gson exposeGson();
    Navigator exposeNavigator();
    JobManager exposeJobManager();

    void inject(WeatherJobCreator weatherJobCreator);
    void inject(CurrentConditionsFragment currentConditionsFragment);

}
