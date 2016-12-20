package com.mariebyleen.weather.weather_display.model.forecast;

public class ForecastResponseCity {
    private String country;
    private ForecastResponseCityCoord coord;
    private String name;
    private int id;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ForecastResponseCityCoord getCoord() {
        return this.coord;
    }

    public void setCoord(ForecastResponseCityCoord coord) {
        this.coord = coord;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
