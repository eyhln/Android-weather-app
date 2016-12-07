package com.mariebyleen.weather.job;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.mapper.WeatherMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;

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
    public void when_data_received_data_is_saved() {
        CurrentConditions saveConditions = new CurrentConditions();
        saveConditions.setTemperature(100.0);

        job.onNext(saveConditions);

        String conditionsJson = preferences.getString("CurrentConditions", "");
        CurrentConditions retrieveConditions = gson.fromJson(conditionsJson, CurrentConditions.class);
        assertEquals(saveConditions.getTemperature(), retrieveConditions.getTemperature());
    }


}
