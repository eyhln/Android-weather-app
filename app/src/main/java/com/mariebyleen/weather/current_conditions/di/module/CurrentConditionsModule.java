package com.mariebyleen.weather.current_conditions.di.module;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
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
                                                                 CurrentConditionsMapper mapper) {
        return new CurrentConditionsViewModel(preferences, gson);
    }

    @PerActivity
    @Provides
    CurrentConditionsMapper provideCurrentConditionsMapper() {
        return new CurrentConditionsMapper();
    }
}
