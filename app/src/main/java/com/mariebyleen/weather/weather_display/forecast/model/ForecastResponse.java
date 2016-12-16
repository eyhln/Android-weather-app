package com.mariebyleen.weather.weather_display.forecast.model;

public class ForecastResponse {
    private ForecastResponseCity city;
    private int cnt;
    private String cod;
    private double message;
    private ForecastResponseList[] list;

    public ForecastResponseCity getCity() {
        return this.city;
    }

    public void setCity(ForecastResponseCity city) {
        this.city = city;
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

    public double getMessage() {
        return this.message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public ForecastResponseList[] getList() {
        return this.list;
    }

    public void setList(ForecastResponseList[] list) {
        this.list = list;
    }
}
