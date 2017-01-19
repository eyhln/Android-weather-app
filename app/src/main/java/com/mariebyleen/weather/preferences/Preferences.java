package com.mariebyleen.weather.preferences;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import javax.inject.Inject;

public class Preferences {

    private SharedPreferences preferences;
    private Resources resources;
    private Gson gson;

    private SharedPreferences.Editor editor;

    @Inject
    public Preferences (SharedPreferences preferences, Resources resources, Gson gson) {
        this.preferences = preferences;
        this.resources = resources;
        this.gson = gson;
        editor = preferences.edit();
    }

    public String getTag(int tagStringResource) {
        return resources.getString(tagStringResource);
    }

    public void putCoordinates(float lat, float lon) {
        editor.putFloat("lat", lat);
        editor.putFloat("lon", lon);
        editor.commit();
    }

    public float getLatitude() {
        return preferences.getFloat("lat", 0.00f);
    }

    public float getLongitude() {
        return preferences.getFloat("lon", 0.00f);
    }

    public void putWeatherData(WeatherData weatherData) {
        String currentConditionsJson = gson.toJson(weatherData);
        editor.putString("WeatherData", currentConditionsJson);
        editor.apply();
    }

    public WeatherData getWeatherData() {
        String weatherJson = preferences.getString("WeatherData", "");
        WeatherData weatherData = gson.fromJson(weatherJson, WeatherData.class);
        if (weatherData == null || weatherJson.equals(""))
            return new WeatherData();

        return weatherData;
    }

    public String getTempUnitsPreferenceCode() {
        String tag = getTag(R.string.preference_units_of_measurement_key);
        return preferences.getString(tag, "");
    }

    public String getWindSpeedUnits() {
        String tag = getTag(R.string.preference_speed_units_key);
        return preferences.getString(tag, "");
    }

    public int getUpdatePeriod() {
        String tag = getTag(R.string.preference_update_period_key);
        String periodString = preferences.getString(tag, "900000");
        return Integer.parseInt(periodString);
    }

    public boolean isFirstRun() {
        boolean isFirstRun = preferences.getBoolean("FirstRun", true);
        if(isFirstRun) {
            editor.putBoolean("FirstRun", Boolean.FALSE);
            editor.apply();
        }
        return isFirstRun;
    }
}
