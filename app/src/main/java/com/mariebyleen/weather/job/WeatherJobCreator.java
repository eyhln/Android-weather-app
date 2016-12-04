package com.mariebyleen.weather.job;

import android.content.SharedPreferences;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;

import javax.inject.Inject;

public class WeatherJobCreator implements JobCreator {

    private OpenWeatherApiService weatherApiService;
    private CurrentConditionsMapper mapper;
    private SharedPreferences preferences;
    private Gson gson;

    @Inject
    public WeatherJobCreator(OpenWeatherApiService weatherApiService,
                             CurrentConditionsMapper mapper,
                             SharedPreferences preferences, Gson gson) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.preferences = preferences;
        this.gson = gson;
    }

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return new WeatherDataUpdateJob(weatherApiService, mapper, preferences, gson);
        throw new IllegalArgumentException("Invalid job tag");
    }
}
