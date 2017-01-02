package com.mariebyleen.weather.weather_display.model.mapped;

public class DailyForecast {

    private long time;

    private double minTemp;
    private double maxTemp;

    private String description;
    private int iconResourcesId;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResourcesId() {
        return iconResourcesId;
    }

    public void setIconResourcesId(int iconResourcesId) {
        this.iconResourcesId = iconResourcesId;
    }
}
