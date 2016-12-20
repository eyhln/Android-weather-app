package com.mariebyleen.weather.weather_display.model.use;

public class DailyForecast {

    private long time;

    private double minTemp;
    private double maxTemp;

    private int iconResourcesId;

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

    public int getIconResourcesId() {
        return iconResourcesId;
    }

    public void setIconResourcesId(int iconResourcesId) {
        this.iconResourcesId = iconResourcesId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
