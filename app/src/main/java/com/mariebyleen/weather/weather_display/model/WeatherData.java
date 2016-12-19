package com.mariebyleen.weather.weather_display.model;

public class WeatherData {

    private long updateTime;

    private double temperature;
    private double humidity;
    private double windSpeed;
    private double windDirection;
    private long sunriseTime;
    private long sunsetTime;

    private int iconResourceId;

    private String description;
    private String cityName;
    private String country;

    private DailyForecast[] forecasts;
    private double[] forecastMaxTemps;
    private double[] forecastMinTemps;
    private int[] forecastIconResourceIds;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public double[] getForecastMaxTemps() {
        return forecastMaxTemps;
    }

    public void setForecastMaxTemps(double[] forecastMaxTemps) {
        this.forecastMaxTemps = forecastMaxTemps;
    }

    public double[] getForecastMinTemps() {
        return forecastMinTemps;
    }

    public void setForecastMinTemps(double[] forecastMinTemps) {
        this.forecastMinTemps = forecastMinTemps;
    }

    public int[] getForecastIconResourceIds() {
        return forecastIconResourceIds;
    }

    public void setForecastIconResourceIds(int[] forecastIconResourceIds) {
        this.forecastIconResourceIds = forecastIconResourceIds;
    }

    public DailyForecast[] getForecasts() {
        return forecasts;
    }

    public void setForecasts(DailyForecast[] forecasts) {
        this.forecasts = forecasts;
    }
}
