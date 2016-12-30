package com.mariebyleen.weather.weather_display.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DisplayDataFormatter {

    private final double KELVIN_TO_CELSIUS = 273.15;

    private Locale locale;

    @Inject
    public DisplayDataFormatter() {
        locale = Locale.getDefault();
    }

    public String convertTemp(double temp, boolean unitsPrefSetToFahrenheit) {
        double convertedTemp;
        if (unitsPrefSetToFahrenheit)
            convertedTemp = getTemperatureInFahrenheit(temp);
        else
            convertedTemp = temp - KELVIN_TO_CELSIUS;
        return String.valueOf(NumberFormat.getInstance().format(Math.round(convertedTemp)));
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return (temperature - KELVIN_TO_CELSIUS)*1.8000 + 32.00;
    }

    public String formatTimeFromEpoch(long unixTime) {
        DateFormat format = new SimpleDateFormat("h:mm a", locale);
        Date updateTime = new Date(unixTime*1000);
        return format.format(updateTime);
    }

    public String formatHumidity(double humidity) {
        double roundHumidity = Math.round(humidity) / 100.0;
        return NumberFormat.getPercentInstance().format(roundHumidity);
    }

    public String formatDescription(String description) {
        if (description != null) {
        String firstLetter = description.substring(0,1);
        String remainder = description.substring(1, description.length());
        return firstLetter.toUpperCase() + remainder;
        }
        return "";
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
