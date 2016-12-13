package com.mariebyleen.weather.model;

import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;

public class WeatherData {

    private CurrentConditionsResponse currentConditions;
    private ForecastResponse forecast;

    public WeatherData(CurrentConditionsResponse currentConditionsResponse,
                       ForecastResponse forecastResponse) {
        this.currentConditions = currentConditionsResponse;
        this.forecast = forecastResponse;
    }

    public CurrentConditionsResponse getCurrentConditions() {
        return currentConditions;
    }

    public ForecastResponse getForecast() {
        return forecast;
    }
}
