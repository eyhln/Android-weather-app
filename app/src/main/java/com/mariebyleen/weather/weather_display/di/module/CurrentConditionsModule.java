package com.mariebyleen.weather.weather_display.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.WeatherDataService;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(SharedPreferences preferences,
                                                                 WeatherDataService service,
                                                                 Gson gson,
                                                                 Context context) {
        return new CurrentConditionsViewModel(preferences, gson, service, context);
    }

    @PerActivity
    @Provides
    WeatherDataService provideWeatherDataService(JobManager jobManager,
                                                 SharedPreferences preferences) {
        return new WeatherDataService(jobManager, preferences);
    }
}
