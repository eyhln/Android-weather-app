package com.mariebyleen.weather.weather_display.di.component;

import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.weather_display.current_conditions.view.CurrentConditionsFragment;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;

import dagger.Component;

@PerActivity
@Component(modules = CurrentConditionsModule.class,
            dependencies = ApplicationComponent.class)
public interface CurrentConditionsComponent {

    void inject(CurrentConditionsFragment fragment);
}