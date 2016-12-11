package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;
import com.mariebyleen.weather.model.WeatherData;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";

    private SharedPreferences preferences;
    private Gson gson;
    private JobManager jobManager;
    private Context context;

    private WeatherData weatherData;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      Gson gson,
                                      JobManager jobManager,
                                      Context context) {
        this.preferences = preferences;
        this.gson = gson;
        this.jobManager = jobManager;
        this.context = context;
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {

        if (weatherData == null) {
            Log.d(TAG, "populating data from memory");
            weatherData = getSavedWeatherData();
        }

        if (jobManager.getAllJobRequestsForTag("WeatherDataUpdateJob").size() == 0)
            Log.d(TAG, "Scheduling period updates");
            jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences));
    }

    public void onViewPause() {
        saveWeatherData(weatherData);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (s.equals("WeatherData")) {
            weatherData = getSavedWeatherData();
            notifyChange();
        }
    }

    private void saveWeatherData(WeatherData weatherData) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String weatherJson = gson.toJson(weatherData);
        prefsEditor.putString("WeatherData", weatherJson);
        prefsEditor.apply();
    }

    private WeatherData getSavedWeatherData() {
        String weatherJson = preferences.getString("CurrentConditions", "");
        if (weatherJson.equals(""))
            return new WeatherData();
        return gson.fromJson(weatherJson,
                WeatherData.class);
    }

    @Bindable
    public String getTemperature() {
        double temperature = weatherData.getTemperature();
        long roundedTemperature = Math.round(temperature);
        Log.d(TAG, "getTemperature called: " + temperature);
        NumberFormat format = NumberFormat.getInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setDecimalSeparatorAlwaysShown(true);
            format.setMinimumFractionDigits(1);
        }
        return String.valueOf(format.format(temperature));
    }

    @Bindable
    public String getDegreesIndicator() {
        return null;
    }
}
