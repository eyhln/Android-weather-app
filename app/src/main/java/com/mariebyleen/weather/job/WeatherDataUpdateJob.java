package com.mariebyleen.weather.job;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;
import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class WeatherDataUpdateJob extends Job implements Observer<CurrentConditions> {

    public final static String TAG = "WeatherDataUpdateJob";

    @Inject
    OpenWeatherApiService weatherApiService;
    @Inject
    CurrentConditionsMapper mapper;
    @Inject
    Gson gson;
    @Inject
    SharedPreferences preferences;

    private SharedPreferences.Editor prefsEditor;

    public WeatherDataUpdateJob() {
        getApplicationComponent().inject(this);
        prefsEditor = preferences.edit();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        weatherApiService.getCurrentConditions(getApiKey())
                .map(new Func1<CurrentConditionsResponse, CurrentConditions>() {
                    @Override
                    public CurrentConditions call(CurrentConditionsResponse currentConditionsResponse) {
                        return mapper.mapCurrentConditions(currentConditionsResponse);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this);

        return Result.SUCCESS;
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
    }

    @Override
    public void onNext(CurrentConditions currentConditions) {
        saveData(currentConditions);
    }

    private void saveData(CurrentConditions conditions) {
        Log.d(TAG, "Data saved to shared preferences");
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);
        prefsEditor.apply();
    }
}
