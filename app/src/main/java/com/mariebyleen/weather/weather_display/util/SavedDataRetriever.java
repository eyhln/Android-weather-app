package com.mariebyleen.weather.weather_display.util;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.model.use.WeatherData;

import javax.inject.Inject;

public class SavedDataRetriever {

    public String weatherDataTag;
    public String unitsOfMeasurementTag;

    private SharedPreferences preferences;
    private Gson gson;
    private Resources resources;

    @Inject
    public SavedDataRetriever(SharedPreferences preferences,
                              Gson gson,
                              Resources resources) {
        this.preferences = preferences;
        this.gson = gson;
        this.resources = resources;
    }

    public WeatherData getSavedWeatherData() {
        if (weatherDataTag == null)
            weatherDataTag = resources.getString(R.string.preference_weather_data_key);
        String weatherJson = preferences.getString(weatherDataTag, "");
        if (weatherJson.equals(""))
            return new WeatherData();
        return gson.fromJson(weatherJson,
                WeatherData.class);
    }

    public boolean unitsPrefSetToFahrenheit() {
        return ((getUnitsOfMeasurementPreferenceCode()).equals("1"));
    }

    private String getUnitsOfMeasurementPreferenceCode() {
        if (unitsOfMeasurementTag == null)
            unitsOfMeasurementTag =
                    resources.getString(R.string.preference_units_of_measurement_key);
        return preferences.getString(unitsOfMeasurementTag, "");
    }
}
