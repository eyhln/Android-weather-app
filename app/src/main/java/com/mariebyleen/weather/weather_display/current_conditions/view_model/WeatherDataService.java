package com.mariebyleen.weather.weather_display.current_conditions.view_model;


import android.content.SharedPreferences;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;

import javax.inject.Inject;

public class WeatherDataService {

    private JobManager jobManager;
    private SharedPreferences preferences;

    @Inject
    public WeatherDataService(JobManager jobManager, SharedPreferences preferences) {
        this.jobManager = jobManager;
        this.preferences = preferences;
    }

    public void manageUpdateJobs() {
        if (jobManager.getAllJobRequestsForTag("WeatherDataUpdateJob").size() == 0)
            jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences));
    }
}
