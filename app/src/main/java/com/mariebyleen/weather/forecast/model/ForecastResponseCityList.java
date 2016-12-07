package com.mariebyleen.weather.forecast.model;

public class ForecastResponseCityList {
    private int dt;
    private String dt_txt;
    private ForecastResponseCityListWeather[] weather;
    private ForecastResponseCityListMain main;
    private ForecastResponseCityListClouds clouds;
    private ForecastResponseCityListSys sys;
    private ForecastResponseCityListWind wind;

    public int getDt() {
        return this.dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return this.dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public ForecastResponseCityListWeather[] getWeather() {
        return this.weather;
    }

    public void setWeather(ForecastResponseCityListWeather[] weather) {
        this.weather = weather;
    }

    public ForecastResponseCityListMain getMain() {
        return this.main;
    }

    public void setMain(ForecastResponseCityListMain main) {
        this.main = main;
    }

    public ForecastResponseCityListClouds getClouds() {
        return this.clouds;
    }

    public void setClouds(ForecastResponseCityListClouds clouds) {
        this.clouds = clouds;
    }

    public ForecastResponseCityListSys getSys() {
        return this.sys;
    }

    public void setSys(ForecastResponseCityListSys sys) {
        this.sys = sys;
    }

    public ForecastResponseCityListWind getWind() {
        return this.wind;
    }

    public void setWind(ForecastResponseCityListWind wind) {
        this.wind = wind;
    }
}
