package com.mariebyleen.weather.application;

import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponseMain;

public class InitUtils {

    protected static CurrentConditionsResponse createDefaultModel() {
        CurrentConditionsResponse response = new CurrentConditionsResponse();
        CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
        main.setTemp(0);
        main.setHumidity(0);
        main.setPressure(0);
        return response;
    }
}
