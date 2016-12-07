package com.mariebyleen.weather.current_conditions.di.module;

import android.content.SharedPreferences;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.mapper.WeatherMapper;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(SharedPreferences preferences,
                                                                 Gson gson,
                                                                 JobManager jobManager) {
        return new CurrentConditionsViewModel(preferences, gson, jobManager);
    }

    @PerActivity
    @Provides
    WeatherMapper provideCurrentConditionsMapper() {
        return new WeatherMapper();
    }
}
