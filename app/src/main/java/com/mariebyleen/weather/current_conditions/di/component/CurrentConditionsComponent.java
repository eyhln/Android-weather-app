package com.mariebyleen.weather.current_conditions.di.component;

import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;

import dagger.Component;

@PerActivity
@Component(modules = CurrentConditionsModule.class,
            dependencies = ApplicationComponent.class)
public interface CurrentConditionsComponent {

    void inject(CurrentConditionsFragment currentConditionsFragment);
}
