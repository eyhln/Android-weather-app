package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mariebyleen.weather.weather_display.model.use.WeatherData;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;
    private SavedDataRetriever savedData;
    private DisplayDataFormatter formatter;

    private WeatherData weatherData;


    private boolean useFahrenheitState;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      SavedDataRetriever savedData,
                                      DisplayDataFormatter formatter) {
        this.preferences = preferences;
        this.savedData = savedData;
        this.formatter = formatter;
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {
        if (weatherData == null) {
            weatherData = savedData.getSavedWeatherData();
            useFahrenheitState = savedData.unitsPrefSetToFahrenheit();
        }
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(savedData.weatherDataTag)) {
            weatherData = savedData.getSavedWeatherData();
            notifyChange();
        }
        if (s.equals(savedData.unitsOfMeasurementTag)) {
            useFahrenheitState = savedData.unitsPrefSetToFahrenheit();
            notifyChange();
        }
    }

    @Bindable
    public String getDescription() {
        return formatter.formatDescription(weatherData.getDescription());
    }

    @Bindable
    public String getTemperature() {
        return formatter.convertTemp(weatherData.getTemperature(),
                savedData.unitsPrefSetToFahrenheit());
    }

    @Bindable
    public boolean getUseFahrenheit() {
        return useFahrenheitState;
    }

    @Bindable
    public String getHumidity() {
        return formatter.formatHumidity(weatherData.getHumidity());
    }

    @Bindable
    public String getWindSpeed() {
        double windspeed = weatherData.getWindSpeed();
        return null;
    }

    @Bindable
    public String getUpdateTime() {
        return formatter.formatTimeFromEpoch(weatherData.getUpdateTime());
    }

    @Bindable
    public String getSunriseTime() {
        return formatter.formatTimeFromEpoch(weatherData.getSunriseTime());
    }

    @Bindable
    public String getSunsetTime() {
        return formatter.formatTimeFromEpoch(weatherData.getSunsetTime());
    }

    @Bindable
    public boolean getIsDay() {
        long sunrise = weatherData.getSunriseTime();
        long sunset = weatherData.getSunsetTime();
        long updateTime = weatherData.getUpdateTime();
        return (sunrise < updateTime && updateTime < sunset);
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}
