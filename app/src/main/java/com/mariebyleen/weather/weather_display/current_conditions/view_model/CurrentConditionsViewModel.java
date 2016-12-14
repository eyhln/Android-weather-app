package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.google.gson.Gson;
import com.mariebyleen.weather.model.WeatherData;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CurrentConditionsVM";
    private static final double KELVIN_TO_CELSIUS = 273.15;
    private final static String weatherDataTag = "WeatherData";
    private final static String unitsOfMeasurementTag = "UNITS";

    private SharedPreferences preferences;
    private Gson gson;
    private WeatherDataService service;
    private Context context;

    private WeatherData weatherData;
    private Locale locale;

    public final ObservableBoolean useFahrenheit =
            new ObservableBoolean(localeUsesFahrenheit(Locale.getDefault()));

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      Gson gson,
                                      WeatherDataService service,
                                      Context context) {
        this.preferences = preferences;
        this.gson = gson;
        this.service = service;
        this.context = context;
        locale = Locale.getDefault();
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {

        if (weatherData == null) {
            weatherData = getSavedWeatherData();
        }

        service.manageUpdateJobs();
    }

    public void onViewPause() {
        saveWeatherData(weatherData);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(weatherDataTag)) {
            weatherData = getSavedWeatherData();
            notifyChange();
        }
        if (s.equals(unitsOfMeasurementTag)) {

        }

    }

    public WeatherData getSavedWeatherData() {
        String weatherJson = preferences.getString(weatherDataTag, "");
        if (weatherJson.equals(""))
            return new WeatherData();
        return gson.fromJson(weatherJson,
                WeatherData.class);
    }

    public void saveWeatherData(WeatherData weatherData) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String weatherJson = gson.toJson(weatherData);
        prefsEditor.putString(weatherDataTag, weatherJson);
        prefsEditor.apply();
    }

    public String getUnitsOfMeasurementPreferenceCode() {
        return preferences.getString(unitsOfMeasurementTag, "");
    }

    @Bindable
    public String getTemperature() {
        double temp = weatherData.getTemperature();
        double convertedTemp;
        if (unitsPrefSetToFahrenheit())
            convertedTemp = getTemperatureInFahrenheit(temp);
        else
            convertedTemp = temp - KELVIN_TO_CELSIUS;
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedTemp)));
    }

    private boolean unitsPrefSetToFahrenheit() {
        if ((getUnitsOfMeasurementPreferenceCode()).equals("1"))
            return true;
        return false;
    }

    private boolean localeUsesFahrenheit(Locale locale) {
        String countryCode = locale.getCountry();
        String[] countriesThatUseFahrenheit = {"US", "BS", "BZ", "KY"};
        for (int i = 0; i < countriesThatUseFahrenheit.length; i++) {
            if (countryCode.equals(countriesThatUseFahrenheit[i]))
                return true;
        }
        return false;
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return (temperature - KELVIN_TO_CELSIUS)*1.8000 + 32.00;
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
        DateFormat format = new SimpleDateFormat("H:mm a", locale);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date updateTime = new Date(weatherData.getUpdateTime());

        long updateTimeUnix = weatherData.getUpdateTime();
        int offset = TimeZone.getDefault().getOffset(updateTimeUnix);
        updateTimeUnix += offset;
        Date updateTimeHuman = new Date(updateTimeUnix);

        return format.format(updateTimeHuman);
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
