package com.mariebyleen.weather.location.di.component;

import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.location.activity.LocationActivity;
import com.mariebyleen.weather.location.di.module.LocationModule;

import dagger.Component;

@Component(modules = LocationModule.class,
        dependencies = ApplicationComponent.class)
@PerActivity
public interface LocationComponent {

    void inject(LocationActivity locationActivity);
}
