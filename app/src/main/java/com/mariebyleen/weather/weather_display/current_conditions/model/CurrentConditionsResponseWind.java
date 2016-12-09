package com.mariebyleen.weather.weather_display.current_conditions.model;

public class CurrentConditionsResponseWind {
    private double deg;
    private double speed;

    public double getDeg() {
        return this.deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
