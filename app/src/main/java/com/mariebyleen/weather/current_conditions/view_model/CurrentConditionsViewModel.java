package com.mariebyleen.weather.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
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
    private CurrentConditionsResponse response;

    @Inject
    public CurrentConditionsViewModel(OpenWeatherApiService weatherApiService, Gson gson,
                                      SharedPreferences preferences) {
        this.weatherApiService = weatherApiService;
        this.gson = gson;
        this.preferences = preferences;
        if (response == null) {
            populateData();
        }
        refreshWeatherData();
    }

    private void populateData() {
        String json = preferences.getString("CurrentConditions", "");
        CurrentConditionsResponse conditions = gson.fromJson(json,
                CurrentConditionsResponse.class);
        response = conditions;
    }

    private void refreshWeatherData() {
        Observable<CurrentConditionsResponse> response =
                weatherApiService.getCurrentConditions(getApiKey());
        response
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
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
        response = currentConditionsResponse;
        Log.d(TAG, "Temperature: " + currentConditionsResponse.getMain().getTemp());
        Log.d(TAG, "Location: " + currentConditionsResponse.getName());
        notifyChange();
    }

    @Bindable
    public String getTemperature() {
        if (response != null) {
            double temperature = response.getMain().getTemp();
            Log.d(TAG, "getTemperature method called");
            return "Temperature: " + String.valueOf(temperature);
        }
        else
            return "";
    }
}
