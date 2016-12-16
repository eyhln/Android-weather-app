package com.mariebyleen.weather.weather_display.forecast.model;

public class ForecastResponseList {
    private int dt;
    private ForecastResponseListTemp temp;
    private ForecastResponseListWeather[] weather;
    private int humidity;
    private double pressure;

    public int getDt() {
        return this.dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public ForecastResponseListTemp getTemp() {
        return this.temp;
    }

    public void setTemp(ForecastResponseListTemp temp) {
        this.temp = temp;
    }

    public ForecastResponseListWeather[] getWeather() {
        return this.weather;
    }

    public void setWeather(ForecastResponseListWeather[] weather) {
        this.weather = weather;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
