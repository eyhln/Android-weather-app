package com.mariebyleen.weather.forecast.model;

public class ForecastResponseCity {
    private String country;
    private ForecastResponseCityCoord coord;
    private String name;
    private int cnt;
    private String cod;
    private int id;
    private double message;
    private ForecastResponseCityList[] list;

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

    public int getCnt() {
        return this.cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCod() {
        return this.cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMessage() {
        return this.message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public ForecastResponseCityList[] getList() {
        return this.list;
    }

    public void setList(ForecastResponseCityList[] list) {
        this.list = list;
    }
}
