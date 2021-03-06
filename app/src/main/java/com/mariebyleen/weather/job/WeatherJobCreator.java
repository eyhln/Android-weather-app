package com.mariebyleen.weather.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.preferences.Preferences;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class WeatherJobCreator implements JobCreator {

    @Inject
    OpenWeatherCaller caller;
    @Inject
    Preferences preferences;

    public WeatherJobCreator() {
        getApplicationComponent().inject(this);
    }

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return new WeatherDataUpdateJob(caller, preferences);
        throw new IllegalArgumentException("Invalid job tag");
    }
}
