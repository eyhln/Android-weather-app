package com.mariebyleen.weather.current_conditions.mapper;

import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

public class CurrentConditionsMapper {

    @Inject
    public CurrentConditionsMapper() {}

    public CurrentConditions mapCurrentConditions(CurrentConditionsResponse response) {
        CurrentConditions currentConditions = new CurrentConditions();
        currentConditions.setTemperature(response.getMain().getTemp());
        return currentConditions;
    }
}
