package com.mariebyleen.weather.weather_display.forecast.view_model;

import android.databinding.BaseObservable;

import com.mariebyleen.weather.weather_display.model.use.DailyForecast;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;

public class ForecastViewModel extends BaseObservable {

    private DailyForecast forecast;
    private DisplayDataFormatter formatter;

    public ForecastViewModel(DailyForecast forecast,
                             DisplayDataFormatter formatter) {
        this.forecast = forecast;
        this.formatter = formatter;
    }

    public DailyForecast getForecast() {
        return forecast;
    }

    public void setForecast(DailyForecast forecast) {
        this.forecast = forecast;
    }
}
