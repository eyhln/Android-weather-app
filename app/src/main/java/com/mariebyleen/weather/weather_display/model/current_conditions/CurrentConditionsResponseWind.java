package com.mariebyleen.weather.weather_display.model.current_conditions;

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
