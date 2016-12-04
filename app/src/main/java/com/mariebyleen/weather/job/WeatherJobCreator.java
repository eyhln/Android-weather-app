package com.mariebyleen.weather.job;

import android.content.SharedPreferences;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;

import javax.inject.Inject;

public class WeatherJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return new WeatherDataUpdateJob();
        throw new IllegalArgumentException("Invalid job tag");
    }
}
