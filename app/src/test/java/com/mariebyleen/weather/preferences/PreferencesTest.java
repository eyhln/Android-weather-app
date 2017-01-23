package com.mariebyleen.weather.preferences;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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
        assertEquals(12.345f, sharedPreferences.getFloat("lon", 0.00f));
    }

    @Test
    public void testGetLongitude() {
        sharedPreferences.edit().putFloat("lon", -12.345f).apply();
        float value = preferences.getLongitude();
        assertEquals(-12.345f, value);
    }

    @Test
    public void testPutWeatherData() {
        WeatherData weatherData = createFakeWeatherData();

        preferences.putWeatherData(weatherData);

        WeatherData retrieveWeatherData = getSavedWeatherData();
        assertNotNull(retrieveWeatherData);
        assertTemperaturesMatch(weatherData, retrieveWeatherData);
    }

    private WeatherData getSavedWeatherData() {
        String weatherJson = sharedPreferences.getString("WeatherData", "");
        return gson.fromJson(weatherJson, WeatherData.class);
    }

    @Test
    public void testGetWeatherData() {
        WeatherData weatherData = createFakeWeatherData();
        saveWeatherData(weatherData);

        WeatherData savedWeatherData = preferences.getWeatherData();

        assertNotNull(savedWeatherData);
        assertTemperaturesMatch(weatherData, savedWeatherData);
    }

    @Test
    public void testGetNullWeatherData() {
        saveWeatherData(null);

        WeatherData savedWeatherData = preferences.getWeatherData();
        assertNotNull(savedWeatherData);
    }

    @Test
    public void testGetUnsavedWeatherData() {
        WeatherData savedWeatherData = preferences.getWeatherData();
        assertNotNull(savedWeatherData);
    }

    private WeatherData createFakeWeatherData() {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(100.0);
        return weatherData;
    }

    private void saveWeatherData(WeatherData weatherData) {
        String currentConditionsJson = gson.toJson(weatherData);
        sharedPreferences.edit().putString("WeatherData", currentConditionsJson).apply();
    }

    private void assertTemperaturesMatch(WeatherData expected, WeatherData test) {
        assertEquals(expected.getTemperature(), test.getTemperature());
    }

    @Test
    public void testGetTempUnitsPreferenceCode() {
        String expected = "Fahrenheit";
        sharedPreferences.edit().putString("UNITS", expected).apply();
        when(resources.getString(R.string.preference_units_of_measurement_key)).thenReturn("UNITS");
        String code = preferences.getTempUnitsPreferenceCode();
        assertEquals(expected, code);
    }

    @Test
    public void testGetWindSpeedUnits() {
        String expected = "mph";
        sharedPreferences.edit().putString("SPEED_UNITS", expected).apply();
        when(resources.getString(R.string.preference_speed_units_key)).thenReturn("SPEED_UNITS");
        String units = preferences.getWindSpeedUnits();
        assertEquals(expected, units);
    }

    @Test
    public void testGetUpdatePeriod() {
        sharedPreferences.edit().putString("UPDATE_PERIOD", "1500").apply();
        when(resources.getString(R.string.preference_update_period_key)).thenReturn("UPDATE_PERIOD");
        int period =  preferences.getUpdatePeriod();
        assertEquals(1500, period);
    }

    @Test
    public void testGetUnsavedUpdatePeriodReturnsDefault() {
        when(resources.getString(R.string.preference_update_period_key)).thenReturn("UPDATE_PERIOD");
        int period = preferences.getUpdatePeriod();
        assertEquals(900000, period);
    }

}
