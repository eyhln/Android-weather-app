package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;
import com.mariebyleen.weather.model.Weather;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";

    private SharedPreferences preferences;
    private Gson gson;
    private JobManager jobManager;

    private Weather weather;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      Gson gson,
                                      JobManager jobManager) {
        this.preferences = preferences;
        this.gson = gson;
        this.jobManager = jobManager;
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {

        if (weather == null) {
            Log.d(TAG, "populating data from memory");
            weather = getSavedWeatherData();
        }

        if (jobManager.getAllJobRequestsForTag("WeatherDataUpdateJob").size() == 0)
            Log.d(TAG, "Scheduling period updates");
            jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences));
    }

    public void onViewPause() {
        saveWeatherData(weather);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (s.equals("Weather")) {
            weather = getSavedWeatherData();
            notifyChange();
        }
    }

    private void saveWeatherData(Weather weather) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String weatherJson = gson.toJson(weather);
        prefsEditor.putString("Weather", weatherJson);
        prefsEditor.apply();
    }

    private Weather getSavedWeatherData() {
        String weatherJson = preferences.getString("CurrentConditions", "");
        if (weatherJson.equals(""))
            return new Weather();
        return gson.fromJson(weatherJson,
                Weather.class);
    }

    @Bindable
    public String getTemperature() {
        double temperature = weather.getTemperature();
        Log.d(TAG, "getTemperature called: " + temperature);
        return String.valueOf(temperature);
    }
}
