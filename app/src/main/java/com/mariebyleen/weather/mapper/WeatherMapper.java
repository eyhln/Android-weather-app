package com.mariebyleen.weather.mapper;

import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.forecast.model.ForecastResponse;

import javax.inject.Inject;

public class WeatherMapper {

    @Inject
    public WeatherMapper() {}

    public CurrentConditions mapCurrentConditions(CurrentConditionsResponse ccResponse,
                                                  ForecastResponse fResponse) {
        CurrentConditions currentConditions = new CurrentConditions();
        currentConditions.setTemperature(ccResponse.getMain().getTemp());
        return currentConditions;
    }
}
