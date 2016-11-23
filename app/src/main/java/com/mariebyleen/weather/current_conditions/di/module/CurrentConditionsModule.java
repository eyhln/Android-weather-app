package com.mariebyleen.weather.current_conditions.di.module;

import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(OpenWeatherApiService apiService) {
        return new CurrentConditionsViewModel(apiService);
    }
}
