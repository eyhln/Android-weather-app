package com.mariebyleen.weather.application;

import android.app.Application;

import com.mariebyleen.weather.application.di.component.AndroidComponent;
import com.mariebyleen.weather.application.di.component.DaggerAndroidComponent;
import com.mariebyleen.weather.application.di.module.AndroidModule;

public class WeatherApplication extends Application {

  private static AndroidComponent androidComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAndroidComponent();
  }

  private void initializeAndroidComponent() {
    androidComponent = DaggerAndroidComponent.builder()
            .androidModule(new AndroidModule(this))
            .build();
  }

  public static AndroidComponent getAndroidComponent() {
    return androidComponent;
  }

}
