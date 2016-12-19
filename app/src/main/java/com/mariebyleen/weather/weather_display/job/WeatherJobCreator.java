package com.mariebyleen.weather.weather_display.job;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class WeatherJobCreator implements JobCreator {

    @Inject
    OpenWeatherApiService weatherApiService;
    @Inject
    WeatherMapper mapper;
    @Inject
    Gson gson;
    @Inject
    SharedPreferences preferences;
    @Inject
    Resources resources;

    public WeatherJobCreator() {
        getApplicationComponent().inject(this);
    }

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return new WeatherDataUpdateJob(weatherApiService, mapper, gson, preferences, resources);
        throw new IllegalArgumentException("Invalid job tag");
    }
}
