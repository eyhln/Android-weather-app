package com.mariebyleen.weather.weather_display.di.component;

import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.weather_display.activity.MainActivity;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.weather_display.forecast.view.ForecastFragment;

import dagger.Component;

@PerActivity
@Component(modules = CurrentConditionsModule.class,
            dependencies = ApplicationComponent.class)
public interface CurrentConditionsComponent {

    void inject(MainActivity mainActivity);

    void inject(ForecastFragment forecastFragment);
}
