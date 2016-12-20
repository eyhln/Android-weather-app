package com.mariebyleen.weather.weather_display.forecast.view_model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mariebyleen.weather.weather_display.model.use.DailyForecast;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForecastViewModel extends BaseObservable {

    private DailyForecast forecast;
    private DisplayDataFormatter formatter;
    private SavedDataRetriever savedData;

    private boolean useFahrenheitState;

    Locale locale;

    public ForecastViewModel(DailyForecast forecast,
                             DisplayDataFormatter formatter,
                             SavedDataRetriever savedData) {
        this.forecast = forecast;
        this.formatter = formatter;
        this.savedData = savedData;
        locale = Locale.getDefault();
        useFahrenheitState = savedData.unitsPrefSetToFahrenheit();
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

    @Bindable
    public String getMinTemp() {
        return formatter.convertTemp(forecast.getMinTemp(), useFahrenheitState);
    }

    @Bindable
    public String getMaxTemp() {
        return formatter.convertTemp(forecast.getMaxTemp(), useFahrenheitState);
    }

    @Bindable
    public boolean getUseFahrenheit() {
        return useFahrenheitState;
    }

    public DailyForecast getForecast() {
        return forecast;
    }
}
