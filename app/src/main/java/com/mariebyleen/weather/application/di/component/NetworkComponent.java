package com.mariebyleen.weather.application.di.component;

import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {

    OpenWeatherApiService exposeOpenWeatherApiService();

}
