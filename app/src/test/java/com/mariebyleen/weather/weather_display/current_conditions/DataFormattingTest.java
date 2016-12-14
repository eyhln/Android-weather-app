package com.mariebyleen.weather.weather_display.current_conditions;


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

    private WeatherData data;
    private Locale testLocale;
    private SharedPreferences preferences;
    private Gson gson;
    private CurrentConditionsViewModel viewModel;

    private final double WARM_DAY_KELVIN = 300.0;
    private final String WARM_DAY_FAHRENHEIT = "80";
    private final String WARM_DAY_CELSIUS = "27";

    @Before
    public void init() {
        preferences = new FakeSharedPreferences();
        gson = new Gson();
        viewModel = new CurrentConditionsViewModel(preferences, gson, weatherDataService);
        data = new WeatherData();
    }

    @Test
    public void EmptyStringPreferenceTemperatureFormat_inCelsius() {
        preferences.edit().putString("UNITS", "0").apply();
        testTemperatureFormat("", WARM_DAY_CELSIUS);
    }

    @Test
    public void TemperatureFormat_inCelsius() {
        testTemperatureFormat("0", WARM_DAY_CELSIUS);
    }

    @Test
    public void TemperatureFormat_inFahrenheit() {
        testTemperatureFormat("1", WARM_DAY_FAHRENHEIT);
    }

    private void testTemperatureFormat(String unitsCode, String expected) {
        preferences.edit().putString("UNITS", unitsCode).apply();
        data.setTemperature(WARM_DAY_KELVIN);
        viewModel.setWeatherData(data);

        String temp = viewModel.getTemperature();

        assertEquals(expected, temp);
    }

    @Test
    public void UnitsIndicator_inCelsius() {
        testUnitsIndicatorFormat("0", false);
    }

    @Test
    public void UnitsIndicator_inFahrenheit() {
        testUnitsIndicatorFormat("1", true);
    }

    private void testUnitsIndicatorFormat(String unitsCode, boolean useFahrenheit) {
        preferences.edit().putString("UNITS", unitsCode).apply();
        viewModel.onViewResume();

        boolean actual = viewModel.getUseFahrenheit();

        assertEquals(useFahrenheit, actual);
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
