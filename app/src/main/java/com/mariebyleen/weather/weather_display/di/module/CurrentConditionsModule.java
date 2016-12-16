package com.mariebyleen.weather.weather_display.di.module;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(SharedPreferences preferences,
                                                                 Gson gson,
                                                                 Resources resources) {
        return new CurrentConditionsViewModel(preferences, gson, resources);
    }
}
