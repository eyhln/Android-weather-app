package com.mariebyleen.weather.weather_display.util;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.view.ForecastViewModel;
import com.mariebyleen.weather.weather_display.model.mapped.DailyForecast;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import javax.inject.Inject;

public class SavedDataRetriever {

    public String weatherDataTag;
    public String temperatureUnitsPrefTag;
    public String speedUnitsPrefTag;

    private SharedPreferences preferences;
    private Gson gson;
    private Resources resources;
    private DisplayDataFormatter formatter;

    private boolean unitsPrefSetToFahrenheit;

    @Inject
    public SavedDataRetriever(SharedPreferences preferences,
                              Gson gson,
                              Resources resources,
                              DisplayDataFormatter formatter) {
        this.preferences = preferences;
        this.gson = gson;
        this.resources = resources;
        this.formatter = formatter;
    }

    public ForecastViewModel[] getForecasts() {
        DailyForecast[] forecasts = getSavedWeatherData().getForecasts();
        if (forecasts != null) {
            ForecastViewModel[] forecastViewModels = new ForecastViewModel[forecasts.length];
            for (int i = 0; i < forecasts.length; i++) {
                forecastViewModels[i] = new ForecastViewModel(forecasts[i], formatter, this);
            }
            return forecastViewModels;
        }
        return new ForecastViewModel[0];
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
        return ((getUnitsOfMeasurementPreferenceCode()).equals("Fahrenheit"));
    }

    private String getUnitsOfMeasurementPreferenceCode() {
        if (temperatureUnitsPrefTag == null)
            temperatureUnitsPrefTag =
                    resources.getString(R.string.preference_units_of_measurement_key);
        return preferences.getString(temperatureUnitsPrefTag, "");
    }

    public String getWindSpeedUnitsPreference() {
        if (speedUnitsPrefTag == null)
            speedUnitsPrefTag = resources.getString(R.string.preference_speed_units_key);
        String speedUnits = preferences.getString(speedUnitsPrefTag, null);
        if (speedUnits != null)
            return speedUnits;
        else return "";
    }
}
