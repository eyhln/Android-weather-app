package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mariebyleen.weather.weather_display.model.WeatherData;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";
    private static final double KELVIN_TO_CELSIUS = 273.15;

    private SharedPreferences preferences;
    private SavedDataRetriever savedData;

    private WeatherData weatherData;
    private Locale locale;

    private boolean useFahrenheitState;

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      SavedDataRetriever savedData) {
        this.preferences = preferences;
        this.savedData = savedData;
        locale = Locale.getDefault();
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
    public String getTemperature() {
        return convertTemp(weatherData.getTemperature());
    }

    @Bindable
    public String getMinTemp() {
        return convertTemp(weatherData.getForecasts()[0].getMinTemp());
    }

    @Bindable
    public String getMaxTemp() {
        return convertTemp(weatherData.getForecasts()[0].getMaxTemp());
    }

    private String convertTemp(double temp) {
        double convertedTemp;
        if (savedData.unitsPrefSetToFahrenheit())
            convertedTemp = getTemperatureInFahrenheit(temp);
        else
            convertedTemp = temp - KELVIN_TO_CELSIUS;
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedTemp)));
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return (temperature - KELVIN_TO_CELSIUS)*1.8000 + 32.00;
    }

    @Bindable
    public boolean getUseFahrenheit() {
        return useFahrenheitState;
    }

    @Bindable
    public String getHumidity() {
        double humidity = Math.round(weatherData.getHumidity()) / 100.0;
        return NumberFormat.getPercentInstance().format(humidity);
    }

    @Bindable
    public String getWindSpeed() {
        double windspeed = weatherData.getWindSpeed();
        return null;
    }

    @Bindable
    public String getUpdateTime() {
        return formatTimeFromEpoch(weatherData.getUpdateTime());
    }

    @Bindable
    public String getSunriseTime() {
        return formatTimeFromEpoch(weatherData.getSunriseTime());
    }

    @Bindable
    public String getSunsetTime() {
        return formatTimeFromEpoch(weatherData.getSunsetTime());
    }

    private String formatTimeFromEpoch(long unixTime) {
        DateFormat format = new SimpleDateFormat("h:mm a", locale);
        Date updateTime = new Date(unixTime*1000);
        return format.format(updateTime);
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

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
