package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";

    private SharedPreferences preferences;
    private Gson gson;
    private JobManager jobManager;

    private CurrentConditions conditions;

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

        if (conditions == null) {
            Log.d(TAG, "populating data from memory");
            conditions = getSavedWeatherData();
        }

        if (jobManager.getAllJobRequestsForTag("WeatherDataUpdateJob").size() == 0)
            Log.d(TAG, "Scheduling period updates");
            jobManager.schedule(WeatherDataUpdateJob.buildJobRequest());
    }

    public void onViewPause() {
        saveWeatherData(conditions);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (s.equals("CurrentConditions")) {
            conditions = getSavedWeatherData();
            notifyChange();
        }
    }

    private void saveWeatherData(CurrentConditions conditions) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);
        prefsEditor.apply();
    }

    private CurrentConditions getSavedWeatherData() {
        String currentConditionsJson = preferences.getString("CurrentConditions", "");
        if (currentConditionsJson.equals(""))
            return new CurrentConditions();
        return gson.fromJson(currentConditionsJson,
                CurrentConditions.class);
    }

    @Bindable
    public String getTemperature() {
        double temperature = conditions.getTemperature();
        Log.d(TAG, "getTemperature called: " + temperature);
        return String.valueOf(temperature);
    }
}
