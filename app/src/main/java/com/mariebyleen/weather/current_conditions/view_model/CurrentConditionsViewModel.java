package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class CurrentConditionsViewModel extends BaseObservable
        implements Observer<CurrentConditionsResponse> {

    private static final String TAG = "CurrentConditionsVM";
    private double lat = 0;
    private double lon = 0;

    private OpenWeatherApiService weatherApiService;
    private Gson gson;
    private SharedPreferences preferences;
    private CurrentConditionsMapper mapper;
    private CurrentConditions conditions;

    @Inject
    public CurrentConditionsViewModel(OpenWeatherApiService weatherApiService,
                                      Gson gson,
                                      SharedPreferences preferences,
                                      CurrentConditionsMapper mapper) {
        this.weatherApiService = weatherApiService;
        this.gson = gson;
        this.preferences = preferences;
        this.mapper = mapper;

        if (conditions == null) {
            populateData();
        }
        refreshWeatherData();
    }

    private void populateData() {
        String currentConditionsJson = preferences.getString("CurrentConditions", "");
        CurrentConditions conditions = gson.fromJson(currentConditionsJson,
                CurrentConditions.class);
        Log.d(TAG, "Conditions: " + conditions.toString());
        Log.d(TAG, "Temp: " + conditions.getTemperature());
        this.conditions = conditions;
    }

    private void refreshWeatherData() {
        Observable<CurrentConditionsResponse> response =
                weatherApiService.getCurrentConditions(getApiKey());
        response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    public void onFragmentPause() {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);
        prefsEditor.apply();
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Current condition weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving current conditions weather data");
        e.printStackTrace();
    }

    @Override
    public void onNext(CurrentConditionsResponse currentConditionsResponse) {
        conditions = mapper.mapCurrentConditions(currentConditionsResponse);
        Log.d(TAG, "Temperature: " + conditions.getTemperature());
        notifyChange();
    }

    @Bindable
    public String getTemperature() {
        if (conditions != null) {
            double temperature = conditions.getTemperature();
            Log.d(TAG, "getTemperature method called");
            return "Temperature: " + String.valueOf(temperature);
        }
        else
            return "";
    }
}
