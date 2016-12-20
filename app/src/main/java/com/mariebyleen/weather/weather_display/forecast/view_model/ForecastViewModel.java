package com.mariebyleen.weather.weather_display.forecast.view_model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mariebyleen.weather.weather_display.model.use.DailyForecast;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastViewModel extends BaseObservable {

    private DailyForecast forecast;
    private DisplayDataFormatter formatter;

    Locale locale;

    public ForecastViewModel(DailyForecast forecast,
                             DisplayDataFormatter formatter) {
        this.forecast = forecast;
        this.formatter = formatter;
        locale = Locale.getDefault();
    }

    @Bindable
    public String getDateDisplayTitle() {
        long forecastTime = forecast.getTime();
        DateFormat format = new SimpleDateFormat("EEEE, MMM d", locale);
        Date updateTime = new Date(forecastTime*1000);
        return format.format(updateTime);
    }

    @Bindable
    public String getDescription() {
        return formatter.formatDescription(forecast.getDescription());
    }

    public DailyForecast getForecast() {
        return forecast;
    }
}
