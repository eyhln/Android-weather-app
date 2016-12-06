package com.mariebyleen.weather.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class WeatherJobCreator implements JobCreator {

    private Job weatherDataUpdateJob;

    public WeatherJobCreator(Job weatherDataUpdateJob) {
        this.weatherDataUpdateJob = weatherDataUpdateJob;
    }

    @Override
    public Job create(String tag) {
        if (tag.equals(WeatherDataUpdateJob.TAG))
            return weatherDataUpdateJob;
        throw new IllegalArgumentException("Invalid job tag");
    }
}
