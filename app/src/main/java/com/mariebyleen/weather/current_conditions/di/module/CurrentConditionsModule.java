package com.mariebyleen.weather.current_conditions.di.module;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(OpenWeatherApiService apiService,
                                                                 Gson gson,
                                                                 SharedPreferences preferences) {
        return new CurrentConditionsViewModel(apiService, gson, preferences);
    }
}
