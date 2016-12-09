package com.mariebyleen.weather.mapper;

import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;
import com.mariebyleen.weather.model.Weather;

import javax.inject.Inject;

public class WeatherMapper {

    @Inject
    public WeatherMapper() {}

    public Weather map(CurrentConditionsResponse ccResponse,
                       ForecastResponse fResponse) {
        Weather weather = new Weather();
        weather.setTemperature(ccResponse.getMain().getTemp());
        weather.setCountry(fResponse.getCity().getCountry());
        return weather;
    }
}
