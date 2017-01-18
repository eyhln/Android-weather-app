package com.mariebyleen.weather.weather_display;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.model.mapped.DailyForecast;
import com.mariebyleen.weather.weather_display.util.DisplayDataFormatter;
import com.mariebyleen.weather.weather_display.util.SavedDataRetriever;
import com.mariebyleen.weather.weather_display.view_model.ForecastViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Locale;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ForecastDataFormattingTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Resources resources;

    private DailyForecast forecast;
    private DisplayDataFormatter formatter;
    private SavedDataRetriever savedData;
    private ForecastViewModel viewModel;
    private Preferences preferences;
    private SharedPreferences sharedPreferences;



    @Before
    public void init() {
        forecast = setUpFakeForecast();
        sharedPreferences = new FakeSharedPreferences();
        preferences = new Preferences(sharedPreferences, resources, null);
        formatter = new DisplayDataFormatter();
        savedData = new SavedDataRetriever(preferences, resources, formatter);
        viewModel = new ForecastViewModel(forecast, formatter, savedData, 0);
    }

    private DailyForecast setUpFakeForecast() {
        DailyForecast forecast = new DailyForecast();
        forecast.setTime(1482253308);
        forecast.setDescription("few clouds");
        return forecast;
    }

    @Test
    public void formatDate_asLongForm() {
        ForecastViewModel viewModel = new ForecastViewModel(forecast, null, savedData, 3);
        TimeZone originalTimeZone = TimeZone.getDefault();
        TimeZone cst = TimeZone.getTimeZone("CST");
        TimeZone.setDefault(cst);
        formatter.setLocale(Locale.US);

        String actual = viewModel.getDateDisplayTitle();

        assertEquals("Tue, Dec 20", actual);

        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    public void formatDate_today() {
        ForecastViewModel viewModel = new ForecastViewModel(forecast, null, savedData, 0);
        when(savedData.getTodayString()).thenReturn("Today");

        String actual = viewModel.getDateDisplayTitle();

        assertEquals("Today", actual);
    }

    @Test
    public void formatDate_Tomorrow() {
        ForecastViewModel viewModel = new ForecastViewModel(forecast, null, savedData, 1);
        when(savedData.getTomorrowString()).thenReturn("Tomorrow");

        String actual = viewModel.getDateDisplayTitle();

        assertEquals("Tomorrow", actual);
    }

    @Test
    public void formatDescription_withInitialCapital() {
        String actual = viewModel.getDescription();
        assertEquals("Few clouds", actual);
    }
}
