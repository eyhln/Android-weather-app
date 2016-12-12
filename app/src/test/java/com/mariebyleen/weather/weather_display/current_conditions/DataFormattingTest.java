package com.mariebyleen.weather.weather_display.current_conditions;


import android.content.Context;
import android.content.SharedPreferences;

import com.mariebyleen.weather.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.WeatherDataService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;

public class DataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @Mock
    SharedPreferences preferences;
    @Mock
    WeatherDataService service;
    @Mock
    Context context;


    private WeatherData data;
    private Locale testLocale;
    private CurrentConditionsViewModel viewModel;

    private final double WARM_DAY_KELVIN = 300.0;
    private final String WARM_DAY_FAHRENHEIT = "80";

    @Before
    public void init() {
        viewModel = new CurrentConditionsViewModel(preferences, service, context);
        data = new WeatherData();
        testLocale = new Locale("", "US");
    }

    @Test
    public void USTemperatureFormat() {
        data.setTemperature(WARM_DAY_KELVIN);
        viewModel.setWeatherData(data);
        viewModel.locale = testLocale;

        String temp = viewModel.getTemperature();

        assertEquals(temp, WARM_DAY_FAHRENHEIT);
    }




}
