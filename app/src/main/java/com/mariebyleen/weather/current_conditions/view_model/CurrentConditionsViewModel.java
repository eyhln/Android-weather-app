package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";

    private SharedPreferences preferences;
    private Gson gson;

    private CurrentConditions conditions;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      Gson gson) {
        this.preferences = preferences;
        this.gson = gson;
    }

    public void onFragmentResume() {
        if (conditions == null) {
            Log.d(TAG, "populating data from memory");
            conditions = getSavedWeatherData();
        }

        new JobRequest.Builder("WeatherDataUpdateJob")
                .setExecutionWindow(500, 1000)
                .build()
                .schedule();

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onFragmentPause() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        saveWeatherData(conditions);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged");
        if (s.equals("CurrentConditions")) {
            conditions = getSavedWeatherData();
            Log.i(TAG, "Data in SharedPreferences updated");
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
        Log.d(TAG, "getTemperature called");
        return "Temperature: " + String.valueOf(temperature);
    }

}
