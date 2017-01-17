package com.mariebyleen.weather.api;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponseMain;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponse;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponseCity;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OpenWeatherCallerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    OpenWeatherApiService weatherApiService;
    @Mock
    Resources resources;

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private WeatherMapper mapper;
    private Preferences preferences;

    private OpenWeatherCaller caller;

    @Before
    public void init() {
        sharedPreferences = new FakeSharedPreferences();
        gson = new Gson();
        mapper = new WeatherMapper();
        preferences = new Preferences(sharedPreferences, resources, gson);
        caller = new OpenWeatherCaller(mapper, weatherApiService, preferences);
    }

    @Test
    @Ignore("need to fix test subscriber")
    public void current_and_forecast_data_are_zipped() {
        setCoordinateValues();
        when(weatherApiService.getCurrentConditions(anyFloat(), anyFloat(), anyString()))
                .thenReturn(getTestCurrentConditionsObservable(100.0));
        when(weatherApiService.getForecast(anyFloat(), anyFloat(), anyInt(), anyString()))
                .thenReturn(getTestForecastObservable("TEST"));
        TestSubscriber<WeatherData> testSubscriber = new TestSubscriber<>();

        caller.getWeatherObservable(0.0f, 0.0f)
                .subscribe(testSubscriber);

        // Empty list of events received -- look up
        List<WeatherData> weatherDataEvents = testSubscriber.getOnNextEvents();
        WeatherData weatherData = weatherDataEvents.get(0);

        testSubscriber.assertNoErrors();
        assertNotNull(weatherData);
        assertEquals(100.0, weatherData.getTemperature());
        assertEquals("TEST", weatherData.getCountry());
    }

        private void setCoordinateValues() {
            FakeSharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("lat", 0.0f);
            editor.putFloat("lon", 0.0f);
        }

        private Observable<CurrentConditionsResponse>
                                        getTestCurrentConditionsObservable(double temp) {
            CurrentConditionsResponse currentConditions = new CurrentConditionsResponse();
            CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
            main.setTemp(temp);
            currentConditions.setMain(main);
            List<CurrentConditionsResponse> cc = new ArrayList<>(1);
            cc.add(currentConditions);
            return Observable.from(cc);
        }

        private Observable<ForecastResponse> getTestForecastObservable(String country) {
            ForecastResponse forecast = new ForecastResponse();
            ForecastResponseCity city = new ForecastResponseCity();
            city.setCountry(country);
            forecast.setCity(city);
            List<ForecastResponse> f = new ArrayList<>(1);
            f.add(forecast);
            return Observable.from(f);
        }

    @Test
    public void when_data_received_data_is_saved() {
        WeatherData saveWeatherData = new WeatherData();
        saveWeatherData.setTemperature(100.0);

        caller.saveData(saveWeatherData);

        WeatherData retrievedWeatherData = getWeatherDataFromPreferences();
        assertEquals(saveWeatherData.getTemperature(), retrievedWeatherData.getTemperature());
    }

    private WeatherData getWeatherDataFromPreferences() {
        String weatherJson = sharedPreferences.getString("WeatherData", "");
        return gson.fromJson(weatherJson, WeatherData.class);
    }
}
