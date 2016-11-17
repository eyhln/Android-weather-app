package com.mariebyleen.weather.base;

import android.app.Application;

import com.mariebyleen.weather.di.component.DaggerAndroidComponent;
import com.mariebyleen.weather.di.module.AndroidModule;

public class WeatherApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAndroidComponent();
  }

  private void initializeAndroidComponent() {
    DaggerAndroidComponent.builder()
            .androidModule(new AndroidModule(this))
            .build();
  }


}
