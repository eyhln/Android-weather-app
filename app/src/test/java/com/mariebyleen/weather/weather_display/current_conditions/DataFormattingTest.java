package com.mariebyleen.weather.weather_display.current_conditions;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.WeatherDataService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class DataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WeatherDataService weatherDataService;
    @Mock
    Context context;

    private WeatherData data;
    private Locale testLocale;
    private SharedPreferences preferences;
    private Gson gson;
    private WeatherDataService service;
    private CurrentConditionsViewModel viewModel;

    private final double WARM_DAY_KELVIN = 300.0;
    private final String WARM_DAY_FAHRENHEIT = "80";
    private final String WARM_DAY_CELSIUS = "27";

    @Before
    public void init() {
        preferences = new FakeSharedPreferences();
        gson = new Gson();
        viewModel = new CurrentConditionsViewModel(preferences, gson, service, context);
        data = new WeatherData();
    }

    @Test
    public void EmptyStringPreferenceTemperatureFormat_inCelsius() {
        preferences.edit().putString("UNITS", "0").apply();
        testTemperatureFormat("", WARM_DAY_CELSIUS);
    }

    @Test
    public void TemperatureFormat_inCelsius() {
        preferences.edit().putString("UNITS", "0").apply();
        testTemperatureFormat("DE", WARM_DAY_CELSIUS);
    }

    @Test
    public void TemperatureFormat_inFahrenheit() {
        preferences.edit().putString("UNITS", "1").apply();
        testTemperatureFormat("US", WARM_DAY_FAHRENHEIT);
    }

    private void testTemperatureFormat(String countryCode, String expected) {
        data.setTemperature(WARM_DAY_KELVIN);
        viewModel.setWeatherData(data);

        String temp = viewModel.getTemperature();

        assertEquals(expected, temp);
    }

    @Test
    public void formatHumidity_asPercentRounded() {
       testFormatHumidity(54.0, "54%");
    }

    @Test
    public void formatHumidity_asPercentZero() {
        testFormatHumidity(0.0, "0%");
    }

    private void testFormatHumidity(double humidity, String expected) {
        data.setHumidity(humidity);
        viewModel.setWeatherData(data);

        String actual = viewModel.getHumidity();

        assertEquals(expected, actual);
    }

    @Test @Ignore
    public void updateTime_asWallClockTime() {
        testLocale = new Locale("", "GB");
        data.setUpdateTime(1);
        fail("implement");
    }
}
