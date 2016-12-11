package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;
import com.mariebyleen.weather.model.WeatherData;

import java.text.NumberFormat;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";
    private static final double KELVIN_TO_CELSIUS = 273.15;

    private SharedPreferences preferences;
    private Gson gson;
    private JobManager jobManager;
    private Context context;

    private WeatherData weatherData;


    private boolean celsius = false;

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
        double temp = weatherData.getTemperature();
        if (celsius)
            temp -= KELVIN_TO_CELSIUS;
        else
            temp = getTemperatureInFahrenheit(temp);
        Log.d(TAG, "getTemperature called: " + temp);
        return String.valueOf(NumberFormat.getInstance().format(Math.round(temp)));
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return 72;
    }

    @Bindable
    public String getDegreesIndicator() {

        if (celsius)
            return context.getString(R.string.degrees_indicator_Celsius);
        return context.getString(R.string.degrees_indicator_Fahrenheit);
    }

    @Bindable
    public String getHumidity() {
        int humidity = weatherData.getHumidity();
        return NumberFormat.getPercentInstance().format(humidity);
    }
}
