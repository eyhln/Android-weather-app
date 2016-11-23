package com.mariebyleen.weather.current_conditions.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class CurrentConditionsResponse extends BaseObservable {
    private int dt;
    private CurrentConditionsResponseCoord coord;
    private CurrentConditionsResponseWeather[] weather;
    private String name;
    private int cod;
    private CurrentConditionsResponseMain main;
    private CurrentConditionsResponseClouds clouds;
    private int id;
    private CurrentConditionsResponseSys sys;
    private CurrentConditionsResponseWind wind;

    public int getDt() {
        return this.dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public CurrentConditionsResponseCoord getCoord() {
        return this.coord;
    }

    public void setCoord(CurrentConditionsResponseCoord coord) {
        this.coord = coord;
    }

    @Bindable
    public CurrentConditionsResponseWeather[] getWeather() {
        return this.weather;
    }

    public void setWeather(CurrentConditionsResponseWeather[] weather) {
        this.weather = weather;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return this.cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Bindable
    public CurrentConditionsResponseMain getMain() {
        return this.main;
    }

    public void setMain(CurrentConditionsResponseMain main) {
        this.main = main;
    }

    @Bindable
    public CurrentConditionsResponseClouds getClouds() {
        return this.clouds;
    }

    public void setClouds(CurrentConditionsResponseClouds clouds) {
        this.clouds = clouds;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrentConditionsResponseSys getSys() {
        return this.sys;
    }

    public void setSys(CurrentConditionsResponseSys sys) {
        this.sys = sys;
    }

    public CurrentConditionsResponseWind getWind() {
        return this.wind;
    }

    public void setWind(CurrentConditionsResponseWind wind) {
        this.wind = wind;
    }
}
