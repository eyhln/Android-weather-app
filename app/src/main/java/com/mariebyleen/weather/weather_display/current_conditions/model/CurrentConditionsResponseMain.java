package com.mariebyleen.weather.weather_display.current_conditions.model;

public class CurrentConditionsResponseMain {

    private double temp;
    private double temp_min;
    private int humidity;
    private int pressure;
    private double temp_max;

    public double getTemp() {
        return this.temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return this.pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getTemp_max() {
        return this.temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getTemp_min() {
        return this.temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }
}
