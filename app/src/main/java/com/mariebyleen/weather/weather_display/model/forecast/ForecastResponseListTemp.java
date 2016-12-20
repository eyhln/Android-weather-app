package com.mariebyleen.weather.weather_display.model.forecast;

public class ForecastResponseListTemp {
    private double min;
    private double max;
    private double eve;
    private double night;
    private double day;
    private double morn;

    public double getMin() {
        return this.min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return this.max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getEve() {
        return this.eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getNight() {
        return this.night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getDay() {
        return this.day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMorn() {
        return this.morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }
}
