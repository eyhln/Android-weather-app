package com.mariebyleen.weather.weather_display;

import com.mariebyleen.weather.weather_display.forecast.view_model.ForecastViewModel;
import com.mariebyleen.weather.weather_display.model.use.DailyForecast;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

public class ForecastDataFormattingTest {

    private DailyForecast forecast;
    private DisplayDataFormatter formatter;
    private ForecastViewModel viewModel;

    @Before
    public void init() {
        forecast = new DailyForecast();
        forecast.setTime(1482253308);
        formatter = new DisplayDataFormatter();
        viewModel = new ForecastViewModel(forecast, formatter);
    }

    @Test
    public void formatDate_asLongForm() {
        TimeZone originalTimeZone = TimeZone.getDefault();
        TimeZone cst = TimeZone.getTimeZone("CST");
        TimeZone.setDefault(cst);
        formatter.setLocale(Locale.US);

        String actual = viewModel.getDateDisplayTitle();

        assertEquals("Tuesday December 20", actual);

        TimeZone.setDefault(originalTimeZone);
    }
}
