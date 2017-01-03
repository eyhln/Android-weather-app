package com.mariebyleen.weather.weather_display.view;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.ToggleButton;

import com.mariebyleen.weather.animation.ResizeAnimation;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;

import java.text.NumberFormat;

import javax.inject.Inject;

public class CurrentConditionsViewModel extends BaseObservable
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final double M_SEC_TO_MI_HR = 2.237;
    private final double M_SEC_TO_KM_HR = 3.600;
    private final double M_SEC_TO_M_SEC = 1;
    private final String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S",
            "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
    private final int DETAIL_VIEW_HEIGHT = 373;

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
        if (s.equals(savedData.weatherDataTag))
            weatherData = savedData.getSavedWeatherData();
        if (s.equals(savedData.temperatureUnitsPrefTag))
            useFahrenheitState = savedData.unitsPrefSetToFahrenheit();
        notifyChange();
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
        double windSpeedMetersPerSecond = weatherData.getWindSpeed();
        String speedUnitsPref = savedData.getWindSpeedUnitsPreference();
        double conversionFactor;
        if (speedUnitsPref.equals("kph"))
            conversionFactor = M_SEC_TO_KM_HR;
        else if (speedUnitsPref.equals("mph"))
            conversionFactor = M_SEC_TO_MI_HR;
        else
            conversionFactor = M_SEC_TO_M_SEC;
        return formatWindSpeed(windSpeedMetersPerSecond, conversionFactor);
    }

    private String formatWindSpeed(double windSpeed, double conversionFactor) {
        double convertedSpeed = Math.round(windSpeed * conversionFactor);
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedSpeed)));
    }

    @Bindable
    public String getWindSpeedUnits() {
        return savedData.getWindSpeedUnitsPreference();
    }

    @Bindable
    public String getWindDirection() {
        double degrees = weatherData.getWindDirection();
        int i = (int)Math.round((degrees + 11.25) /22.5);
        return directions[i % 16];
    }

    @Bindable
    public String getUpdateTime() {
        //return (String)DateUtils.getRelativeTimeSpanString(weatherData.getUpdateTime() * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
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

    public void animateDetailView(ToggleButton toggle, View detailView) {
        if (toggle != null) {
            if (toggle.isChecked())
                animateDetailContent(detailView, DETAIL_VIEW_HEIGHT, 0);
            else
                animateDetailContent(detailView, 0, DETAIL_VIEW_HEIGHT);
        }
    }

    private void animateDetailContent(View detailContent, int startSize, int endSize) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(detailContent, startSize, endSize);
        resizeAnimation.setDuration(300);
        detailContent.startAnimation(resizeAnimation);
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}
