package com.mariebyleen.weather.weather_display.current_conditions.view_model;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

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

    private SharedPreferences preferences;
    private WeatherDataService service;
    private Context context;

    private WeatherData weatherData;
    private Locale locale;

    public final ObservableBoolean useFahrenheit =
            new ObservableBoolean(localeUsesFahrenheit(Locale.getDefault()));

    @Inject
    public CurrentConditionsViewModel(SharedPreferences preferences,
                                      WeatherDataService service,
                                      Context context) {
        this.preferences = preferences;
        this.service = service;
        this.context = context;
        locale = Locale.getDefault();
    }

    public void onViewCreate() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void onViewResume() {

        if (weatherData == null) {
            weatherData = service.getSavedWeatherData();
        }

        service.manageUpdateJobs();
    }

    public void onViewPause() {
        service.saveWeatherData(weatherData);
    }

    public void onViewDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(service.weatherDataTag)) {
            weatherData = service.getSavedWeatherData();
            notifyChange();
        }
    }

    @Bindable
    public String getTemperature() {
        double temp = weatherData.getTemperature();
        double convertedTemp;
        if (localeUsesFahrenheit(locale))
            convertedTemp = getTemperatureInFahrenheit(temp);
        else
            convertedTemp = temp - KELVIN_TO_CELSIUS;
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedTemp)));
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
