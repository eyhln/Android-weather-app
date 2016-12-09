package com.mariebyleen.weather.weather_display.current_conditions;


import com.mariebyleen.weather.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponseMain;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;

public class DataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    private WeatherMapper mapper;
    private CurrentConditionsResponse testResponse;
    private CurrentConditionsViewModel viewModel;

    @Before
    public void init() {
        testResponse = new CurrentConditionsResponse();
        mapper = new WeatherMapper();
    }

    @Test
    public void tempFormattedWithTemperatureLabel() {
        setFakeTemperature(287.1);

        String result = viewModel.getTemperature();

        assertEquals(result, "Temperature: 287.1");
    }

    private CurrentConditionsResponse setFakeTemperature(double temperature) {
        CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
        main.setTemp(temperature);
        testResponse.setMain(main);
        return testResponse;
    }
}
