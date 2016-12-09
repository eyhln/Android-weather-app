package com.mariebyleen.weather.weather_display.forecast.model;

public class ForecastResponse {
    private ForecastResponseCity city;

    public ForecastResponseCity getCity() {
        return this.city;
    }

    public void setCity(ForecastResponseCity city) {
        this.city = city;
    }
}
