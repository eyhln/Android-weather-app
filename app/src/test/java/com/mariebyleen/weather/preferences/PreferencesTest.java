package com.mariebyleen.weather.preferences;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PreferencesTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Mock
    Resources resources;

    private Preferences preferences;

    @Before
    public void init() {
        sharedPreferences = new FakeSharedPreferences();
        gson = new Gson();
        preferences = new Preferences(sharedPreferences, resources, gson);
    }

    @Test
    public void testPutLatitude() {
        preferences.putCoordinates(-12.345f, 0.000f);
        assertEquals(-12.345f, sharedPreferences.getFloat("lat", 0.00f));
    }

    @Test
    public void testGetLatitude() {
        sharedPreferences.edit().putFloat("lat", 12.345f).apply();
        float value =preferences.getLatitude();
        assertEquals(12.345f, value);
    }

    @Test
    public void testPutLongitude() {
        preferences.putCoordinates(0.000f, 12.345f);
        assertEquals(-12.345f, sharedPreferences.getFloat("lon", 0.00f));
    }

    @Test
    public void testGetLongitude() {
        sharedPreferences.edit().putFloat("lon", 12.345f).apply();
        float value = preferences.getLongitude();
        assertEquals(12.345f, value);
    }

    @Test
    public void testPutWeatherData() {
        WeatherData weatherData = createFakeWeatherData();

        preferences.putWeatherData(weatherData);

        String weatherJson = sharedPreferences.getString("WeatherData", "");
        WeatherData retrieveWeatherData = gson.fromJson(weatherJson, WeatherData.class);
        assertNotNull(retrieveWeatherData);
        assertTemperaturesMatch(weatherData, retrieveWeatherData);
    }

    @Test
    public void testGetWeatherData() {
        WeatherData weatherData = createFakeWeatherData();
        String currentConditionsJson = gson.toJson(weatherData);
        sharedPreferences.edit().putString("WeatherData", currentConditionsJson).apply();

        WeatherData savedWeatherData = preferences.getWeatherData();

        assertNotNull(savedWeatherData);
        assertTemperaturesMatch(weatherData, savedWeatherData);
    }

    private WeatherData createFakeWeatherData() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(100.0);
        return weatherData;
    }

    private void assertTemperaturesMatch(WeatherData expected, WeatherData test) {
        assertEquals(expected.getTemperature(), test.getTemperature());
    }

    @Test
    public void testGetTempUnitsPreferenceCode() {

    }
}
