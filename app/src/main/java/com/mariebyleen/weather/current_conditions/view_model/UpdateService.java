package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.update_timer.AutomaticUpdateTimer;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class UpdateService {

    private final static String TAG = "UpdateService";

    private SharedPreferences preferences;
    private SharedPreferences.Editor prefsEditor;
    private Gson gson;
    private AutomaticUpdateTimer timer;
    private OpenWeatherApiService weatherApiService;

    @Inject
    public UpdateService(SharedPreferences preferences, Gson gson, AutomaticUpdateTimer timer,
                         OpenWeatherApiService weatherApiService) {
        this.preferences = preferences;
        this.gson = gson;
        this.timer = timer;
        this.weatherApiService = weatherApiService;
        prefsEditor = preferences.edit();
    }

    public void saveData(CurrentConditions conditions) {
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);

        if (timer.getLastLong() == null)
            prefsEditor.putLong("MostRecentUpdate", -1);
        else {
            long mostRecentUpdate = timer.getLastLong();
            prefsEditor.putLong("MostRecentUpdate", mostRecentUpdate);
        }
        prefsEditor.apply();
    }

    public CurrentConditions getSavedConditions() {
        String currentConditionsJson = preferences.getString("CurrentConditions", "");
        return gson.fromJson(currentConditionsJson,
                CurrentConditions.class);
    }

    public boolean missedMostRecentUpdate() {
        long mostRecentUpdate = preferences.getLong("MostRecentUpdate", -1);
        Log.d(TAG, "most recent update: " + mostRecentUpdate);
        Log.d(TAG, "current timer state: " + timer.getLastLong());

        if (timer.getLastLong() == null)
            prefsEditor.putLong("MostRecentUpdate", -1);

        return (mostRecentUpdate != timer.getLastLong());
    }


    public Observable<CurrentConditionsResponse> getAutomaticUpdateObservable() {
        Observable<Long> counterObservable = timer.getCounterObservable();
        return counterObservable
                .flatMap(new Func1<Long, Observable<CurrentConditionsResponse>>() {
                    @Override
                    public Observable<CurrentConditionsResponse> call(Long aLong) {
                        return getApiCallObservable();
                    }
                });
    }

    public Observable<CurrentConditionsResponse> getManualUpdateObservable() {
        return getApiCallObservable();
    }

    private Observable<CurrentConditionsResponse> getApiCallObservable() {
        return weatherApiService.getCurrentConditions(getApiKey())
                .subscribeOn(timer.getScheduler())
                .observeOn(Schedulers.newThread());
    }
}

