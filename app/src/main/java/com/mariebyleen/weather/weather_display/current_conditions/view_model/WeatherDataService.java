package com.mariebyleen.weather.weather_display.current_conditions.view_model;


import android.content.SharedPreferences;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;
import com.mariebyleen.weather.model.WeatherData;

import javax.inject.Inject;

public class WeatherDataService {

    private final static String TAG = "WeatherDataService";
    public final static String weatherDataTag = "WeatherData";

    private JobManager jobManager;
    private SharedPreferences preferences;
    private Gson gson;

    @Inject
    public WeatherDataService(JobManager jobManager, SharedPreferences preferences, Gson gson) {
        this.jobManager = jobManager;
        this.preferences = preferences;
        this.gson = gson;
    }

    public void manageUpdateJobs() {
        if (jobManager.getAllJobRequestsForTag("WeatherDataUpdateJob").size() == 0)
            Log.d(TAG, "Scheduling period updates");
        jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences));
    }

    public WeatherData getSavedWeatherData() {
        String weatherJson = preferences.getString(weatherDataTag, "");
        if (weatherJson.equals(""))
            return new WeatherData();
        return gson.fromJson(weatherJson,
                WeatherData.class);
    }

    public void saveWeatherData(WeatherData weatherData) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String weatherJson = gson.toJson(weatherData);
        prefsEditor.putString(weatherDataTag, weatherJson);
        prefsEditor.apply();
    }
}
