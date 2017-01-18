package com.mariebyleen.weather.weather_display.di.module;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.activity.ForecastRecyclerAdapter;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;
import com.mariebyleen.weather.weather_display.view_model.CurrentConditionsViewModel;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(SharedPreferences preferences,
                                                                 SavedDataRetriever savedData,
                                                                 DisplayDataFormatter formatter) {
        return new CurrentConditionsViewModel(preferences, savedData, formatter);
    }

    @PerActivity
    @Provides
    ForecastRecyclerAdapter provideForecastRecyclerAdapter(SharedPreferences preferences,
                                                           SavedDataRetriever savedData) {
        return new ForecastRecyclerAdapter(preferences, savedData);
    }

    @PerActivity
    @Provides
    SavedDataRetriever provideSavedDataRetriever(Preferences preferences,
                                                 Resources resources,
                                                 DisplayDataFormatter formatter) {
        return new SavedDataRetriever(preferences, resources, formatter);
    }

    @PerActivity
    @Provides
    DisplayDataFormatter provideDisplayDataFormatter() {
        return new DisplayDataFormatter();
    }
}
