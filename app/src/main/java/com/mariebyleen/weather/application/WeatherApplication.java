package com.mariebyleen.weather.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.application.di.component.ApplicationComponent;
import com.mariebyleen.weather.application.di.component.DaggerApplicationComponent;
import com.mariebyleen.weather.application.di.module.ApplicationModule;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;


public class WeatherApplication extends Application {

  private static ApplicationComponent applicationComponent;
  private static final String baseUrl = "http://api.openweathermap.org/data/2.5/";
  private static final String apiKey = "bb5dd17d68b943dbf98a7512901fcc04";

  private SharedPreferences preferences;

  @Override
  public void onCreate() {
    super.onCreate();
    initializeAndroidComponents();
    checkForFirstRun();
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

  private void checkForFirstRun() {
    preferences = getSharedPreferences("com.mariebyleen.weather", MODE_PRIVATE);
    if (preferences.getBoolean("firstrun", true)) {
      populateSharedPrefsWithDefaultModel();
    }
      preferences.edit().putBoolean("firstrun", false).apply();
  }

  private void populateSharedPrefsWithDefaultModel() {
    CurrentConditions currentConditionsEmpty = InitUtils.createDefaultModel();
    SharedPreferences.Editor prefsEditor = preferences.edit();
    Gson gson = new Gson();
    String currentConditionsJson = gson.toJson(currentConditionsEmpty);
    prefsEditor.putString("CurrentConditions", currentConditionsJson);
    prefsEditor.apply();
  }
}
