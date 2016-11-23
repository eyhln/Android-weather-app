package com.mariebyleen.weather.application.di.component;

import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.di.module.NetworkModule;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {

    OpenWeatherApiService exposeOpenWeatherApiService();

    void inject(CurrentConditionsFragment currentConditionsFragment);
}
