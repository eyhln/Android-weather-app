package com.mariebyleen.weather.mapper;

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
        weatherData.setTemperature(ccResponse.getMain().getTemp());
        weatherData.setHumidity(ccResponse.getMain().getHumidity());
        weatherData.setCountry(fResponse.getCity().getCountry());
        return weatherData;
    }
}
