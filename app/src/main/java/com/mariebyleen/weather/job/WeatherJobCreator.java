package com.mariebyleen.weather.job;

import android.content.SharedPreferences;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.mapper.WeatherMapper;

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

    public WeatherJobCreator() {
        getApplicationComponent().inject(this);
    }

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return new WeatherDataUpdateJob(weatherApiService, mapper, gson, preferences);
        throw new IllegalArgumentException("Invalid job tag");
    }
}
