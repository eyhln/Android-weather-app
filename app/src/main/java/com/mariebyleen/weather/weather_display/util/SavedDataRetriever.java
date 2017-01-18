package com.mariebyleen.weather.weather_display.util;

import android.content.res.Resources;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.model.mapped.DailyForecast;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;
import com.mariebyleen.weather.weather_display.view_model.ForecastViewModel;

import javax.inject.Inject;

public class SavedDataRetriever {

    public String weatherDataTag;
    public String temperatureUnitsPrefTag;

    private Preferences preferences;
    private Resources resources;
    private DisplayDataFormatter formatter;

    @Inject
    public SavedDataRetriever(Preferences preferences,
                              Resources resources,
                              DisplayDataFormatter formatter) {
        this.preferences = preferences;
        this.resources = resources;
        this.formatter = formatter;
    }

    public ForecastViewModel[] getForecasts() {
        DailyForecast[] forecasts = getSavedWeatherData().getForecasts();
        if (forecasts != null) {
            ForecastViewModel[] forecastViewModels = new ForecastViewModel[forecasts.length];
            for (int i = 0; i < forecasts.length; i++) {
                forecastViewModels[i] = new ForecastViewModel(forecasts[i], formatter, this, i);
            }
            return forecastViewModels;
        }
        return new ForecastViewModel[0];
    }

    public WeatherData getSavedWeatherData() {
        return preferences.getWeatherData();
    }

    public boolean unitsPrefSetToFahrenheit() {
        return (preferences.getTempUnitsPreferenceCode().equals("Fahrenheit"));
    }

    public String getWindSpeedUnitsPreference() {
        return preferences.getWindSpeedUnits();
    }

    public String getTodayString() {
        return resources.getString(R.string.today);
    }

    public String getTomorrowString() {
        return resources.getString(R.string.tomorrow);
    }
}
