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
    private final String WARM_DAY_CELSIUS = "27";

    @Before
    public void init() {
        viewModel = new CurrentConditionsViewModel(preferences, service, context);
        data = new WeatherData();
    }

    @Test
    public void EmptyStringLocaleTemperatureFormat_inCelsius() {
        testTemperatureFormat("", WARM_DAY_CELSIUS);
    }

    @Test
    public void GermanyTemperatureFormat_inCelsius() {
        testTemperatureFormat("DE", WARM_DAY_CELSIUS);
    }

    @Test
    public void USTemperatureFormat_inFahrenheit() {
        testTemperatureFormat("US", WARM_DAY_FAHRENHEIT);
    }

    @Test
    public void BahamasTemperatureFormat_inFahrenheit() {
        testTemperatureFormat("BS", WARM_DAY_FAHRENHEIT);
    }

    @Test
    public void CaymanIslandsTemperatureFormat_inFahrenheit() {
        testTemperatureFormat("KY", WARM_DAY_FAHRENHEIT);
    }

    @Test
    public void BelizeTemperatureFormat_inFahrenheit() {
        testTemperatureFormat("BZ", WARM_DAY_FAHRENHEIT);
    }

    private void testTemperatureFormat(String countryCode, String expected) {
        testLocale = new Locale("", countryCode);
        viewModel.setLocale(testLocale);
        data.setTemperature(WARM_DAY_KELVIN);
        viewModel.setWeatherData(data);

        String temp = viewModel.getTemperature();

        assertEquals(expected, temp);
    }

    @Test
    public void formatHumidity_asPercentRounded() {
       testFormatHumidity(0.54444, "54%");
    }

    @Test
    public void formatHumidity_asPercentZero() {
        testFormatHumidity(0, "0%");
    }

    private void testFormatHumidity(double humidity, String expected) {
        data.setHumidity(humidity);
        viewModel.setWeatherData(data);

        String actual = viewModel.getHumidity();

        assertEquals(expected, actual);
    }



}
