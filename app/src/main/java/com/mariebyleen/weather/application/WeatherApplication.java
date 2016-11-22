package com.mariebyleen.weather.application;

import android.app.Application;

import com.mariebyleen.weather.application.di.component.AndroidComponent;
import com.mariebyleen.weather.application.di.component.DaggerAndroidComponent;
import com.mariebyleen.weather.application.di.component.DaggerNetworkComponent;
import com.mariebyleen.weather.application.di.component.NetworkComponent;
import com.mariebyleen.weather.application.di.module.AndroidModule;
import com.mariebyleen.weather.application.di.module.NetworkModule;


public class WeatherApplication extends Application {

  private static AndroidComponent androidComponent;
  private static NetworkComponent networkComponent;
  private static final String baseUrl = "http://api.openweathermap.org/data/2.5";

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAndroidComponents();
  }

  private void initializeAndroidComponents() {
    androidComponent = DaggerAndroidComponent.builder()
            .androidModule(new AndroidModule(this))
            .build();

    networkComponent = DaggerNetworkComponent.builder()
            .networkModule(new NetworkModule(baseUrl))
            .build();
  }

  public static AndroidComponent getAndroidComponent() {
    return androidComponent;
  }
  public static NetworkComponent getNetworkComponent() {
    return networkComponent;
  }

}
