package com.mariebyleen.weather.mapper;

import android.util.Log;

import com.mariebyleen.weather.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;

import javax.inject.Inject;

public class WeatherMapper {

    @Inject
    public WeatherMapper() {}

    public WeatherData map(CurrentConditionsResponse ccResponse,
                           ForecastResponse fResponse) {
        WeatherData weatherData = new WeatherData();
        weatherData.setUpdateTime(ccResponse.getDt());
        weatherData.setSunriseTime(ccResponse.getSys().getSunrise());
        weatherData.setSunsetTime(ccResponse.getSys().getSunset());
        weatherData.setTemperature(ccResponse.getMain().getTemp());
        weatherData.setHumidity(ccResponse.getMain().getHumidity());
        weatherData.setWindSpeed(ccResponse.getWind().getSpeed());
        weatherData.setWindDirection(ccResponse.getWind().getDeg());
        weatherData.setIconIdentifier(ccResponse.getWeather()[0].getIcon());
        weatherData.setDescription(ccResponse.getWeather()[0].getDescription());
        weatherData.setCityName(ccResponse.getName());
        weatherData.setCountry(fResponse.getCity().getCountry());
        return weatherData;
    }
}
