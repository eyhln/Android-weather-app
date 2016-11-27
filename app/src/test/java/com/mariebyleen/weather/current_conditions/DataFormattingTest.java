package com.mariebyleen.weather.current_conditions;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponseMain;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DataFormattingTest {

    @Mock
    private OpenWeatherApiService weatherApiService;
    @Mock
    private SharedPreferences preferences;

    private Gson gson;
    private CurrentConditionsMapper mapper;
    private CurrentConditionsResponse testResponse;
    private CurrentConditionsViewModel viewModel;

    @Before
    public void init() {
        testResponse = new CurrentConditionsResponse();
        gson = new Gson();
        mapper = new CurrentConditionsMapper();
        viewModel = new CurrentConditionsViewModel(weatherApiService, gson, preferences, mapper);
    }

    @Test
    public void tempFormattedWithTemperatureLabel() {
        setFakeTemperature(287.1);

        viewModel.onNext(testResponse);
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
