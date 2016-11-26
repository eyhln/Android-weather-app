package com.mariebyleen.weather.application;

import com.mariebyleen.weather.current_conditions.model.CurrentConditions;

public class InitUtils {

    protected static CurrentConditions createDefaultModel() {
        CurrentConditions conditions = new CurrentConditions();
        conditions.setTemperature(0);
        return conditions;
    }
}
