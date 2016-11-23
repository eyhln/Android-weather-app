package com.mariebyleen.weather.current_conditions.di.component;

import com.mariebyleen.weather.application.di.component.NetworkComponent;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;

import dagger.Component;

@Component(modules = CurrentConditionsModule.class,
            dependencies = NetworkComponent.class)
@PerActivity
public interface CurrentConditionsComponent {

    void inject(CurrentConditionsFragment currentConditionsFragment);
}
