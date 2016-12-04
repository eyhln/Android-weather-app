package com.mariebyleen.weather.application;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.component.DaggerApplicationComponent;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.job.WeatherJobCreator;


public class WeatherApplication extends Application {

  private static ApplicationComponent applicationComponent;
  private static final String baseUrl = "http://api.openweathermap.org/data/2.5/";
  private static final String apiKey = "bb5dd17d68b943dbf98a7512901fcc04";

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAndroidComponents();
    JobManager.create(this).addJobCreator(new WeatherJobCreator());
  }

  private void initializeAndroidComponents() {
    applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this, baseUrl))
            .build();
  }

  public static ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
  public static String getApiKey() {return apiKey;}

}
