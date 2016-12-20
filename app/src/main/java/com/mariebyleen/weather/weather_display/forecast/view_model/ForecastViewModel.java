package com.mariebyleen.weather.weather_display.forecast.view_model;

import android.databinding.BaseObservable;

import com.mariebyleen.weather.weather_display.model.use.DailyForecast;

public class ForecastViewModel extends BaseObservable {

    private DailyForecast forecast;

    public ForecastViewModel(DailyForecast forecast) {
        this.forecast = forecast;
    }

    public DailyForecast getForecast() {
        return forecast;
    }
}
