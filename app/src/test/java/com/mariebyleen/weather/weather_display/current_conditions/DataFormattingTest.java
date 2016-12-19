package com.mariebyleen.weather.weather_display.current_conditions;


import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Resources resources;

    private WeatherData data;
    private SharedPreferences preferences;
    private Gson gson;
    private SavedDataRetriever savedDataRetriever;
    private CurrentConditionsViewModel viewModel;

    private final double WARM_DAY_KELVIN = 300.0;
    private final String WARM_DAY_FAHRENHEIT = "80";
    private final String WARM_DAY_CELSIUS = "27";

    @Before
    public void init() {
        preferences = new FakeSharedPreferences();
        gson = new Gson();
        savedDataRetriever = new SavedDataRetriever(preferences, gson, resources);
        viewModel = new CurrentConditionsViewModel(preferences, savedDataRetriever);
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
        setUpUnitsPreference(unitsCode);
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
        setUpUnitsPreference(unitsCode);

        boolean actual = viewModel.getUseFahrenheit();

        assertEquals(useFahrenheit, actual);
    }

    private void setUpUnitsPreference(String unitsCode) {
        when(resources.getString(R.string.preference_units_of_measurement_key))
                .thenReturn("UNITS");
        preferences.edit().putString("UNITS", unitsCode).apply();
        viewModel.onViewResume();
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

    @Test
    public void formatTime_asWallClockTime() {
        TimeZone originalTimeZone = TimeZone.getDefault();
        TimeZone cst = TimeZone.getTimeZone("CST");
        TimeZone.setDefault(cst);
        viewModel.setLocale(Locale.US);

        data.setUpdateTime(1481729714);
        viewModel.setWeatherData(data);

        String actual = viewModel.getUpdateTime();

        assertEquals("9:35 AM", actual);

        TimeZone.setDefault(originalTimeZone);
    }
}
