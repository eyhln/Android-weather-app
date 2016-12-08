package com.mariebyleen.weather.job;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponseMain;
import com.mariebyleen.weather.forecast.model.ForecastResponse;
import com.mariebyleen.weather.forecast.model.ForecastResponseCity;
import com.mariebyleen.weather.mapper.WeatherMapper;
import com.mariebyleen.weather.model.Weather;

import org.junit.Before;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class WeatherDataUpdateJobTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    OpenWeatherApiService weatherApiService;

    private SharedPreferences preferences;
    private Gson gson;
    private WeatherMapper mapper;

    private WeatherDataUpdateJob job;

    @Before
    public void init() {
        preferences = new FakeSharedPreferences();
        gson = new Gson();
        mapper = new WeatherMapper();
        job = new WeatherDataUpdateJob(weatherApiService, mapper, gson, preferences);
    }

    @Test
    public void current_and_forecast_data_are_zipped() {
        setCoordinateValues();
        when(weatherApiService.getCurrentConditions(anyFloat(), anyFloat(), anyString()))
                .thenReturn(getFakeCurrentConditionsObservable(100.0));
        when(weatherApiService.getForecast(anyFloat(), anyFloat(), anyString()))
                .thenReturn(getFakeForecastObservable("TEST"));
        TestSubscriber<Weather> testSubscriber = new TestSubscriber<>();

        job.getWeatherObservable()
                .subscribe(testSubscriber);

        List<Weather> weatherEvents = testSubscriber.getOnNextEvents();
        Weather weather = weatherEvents.get(0);

        testSubscriber.assertNoErrors();
        assertNotNull(weather);
        assertEquals(100.0, weather.getTemperature());
        assertEquals("TEST", weather.getCountry());
    }

        private void setCoordinateValues() {
            FakeSharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("lat", 0);
            editor.putFloat("lon", 0);
        }

        private Observable<CurrentConditionsResponse>
                                        getFakeCurrentConditionsObservable(double temp) {
            CurrentConditionsResponse currentConditions = new CurrentConditionsResponse();
            CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
            main.setTemp(temp);
            currentConditions.setMain(main);
            List<CurrentConditionsResponse> cc = new ArrayList<>(1);
            cc.add(currentConditions);
            return Observable.from(cc);
        }

        private Observable<ForecastResponse> getFakeForecastObservable(String country) {
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
        Weather saveWeather = new Weather();
        saveWeather.setTemperature(100.0);

        job.onNext(saveWeather);

        String weatherJson = preferences.getString("Weather", "");
        Weather retrieveWeather = gson.fromJson(weatherJson, Weather.class);
        assertEquals(saveWeather.getTemperature(), retrieveWeather.getTemperature());
    }


}
