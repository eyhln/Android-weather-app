package com.mariebyleen.weather.current_conditions.view_model;

import android.databinding.BaseObservable;
import android.util.Log;

import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CurrentConditionsViewModel extends BaseObservable
        implements Observer<CurrentConditionsResponse> {

    private static final String TAG = "CurrentConditionsVM";
    private double lat = 0;
    private double lon = 0;

    private OpenWeatherApiService weatherApiService;
    private CurrentConditionsResponse response;

    @Inject
    public CurrentConditionsViewModel(OpenWeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
        refreshWeatherData();
    }

    private void refreshWeatherData() {
        Observable<CurrentConditionsResponse> response =
                weatherApiService.getCurrentConditions(lat, lon);
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
    }

    @Override
    public void onNext(CurrentConditionsResponse currentConditionsResponse) {
        response = currentConditionsResponse;
        notifyChange();
    }

    public String getTemperature() {
        double temperature = response.getMain().getTemp();
        return String.valueOf(temperature);
    }

    public String getPressure() {
        double pressure = response.getMain().getPressure();
        return String.valueOf(pressure);
    }

    public String getHumidity() {
        double humidity = response.getMain().getHumidity();
        return String.valueOf(humidity);
    }
}
