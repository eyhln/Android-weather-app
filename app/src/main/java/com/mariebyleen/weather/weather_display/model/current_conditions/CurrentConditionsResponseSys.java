package com.mariebyleen.weather.weather_display.model.current_conditions;

public class CurrentConditionsResponseSys {
    private String country;
    private int sunrise;
    private int sunset;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return this.sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }
}
