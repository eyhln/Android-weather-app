package com.mariebyleen.weather.weather_display.current_conditions.view_model;


import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.job.WeatherDataUpdateJob;

import javax.inject.Inject;

public class WeatherDataService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private JobManager jobManager;
    private SharedPreferences preferences;
    private Resources resources;

    @Inject
    public WeatherDataService(JobManager jobManager, SharedPreferences preferences,
                              Resources resources) {
        this.jobManager = jobManager;
        this.preferences = preferences;
        this.resources = resources;
    }

    public void manageUpdateJobs() {
        if (jobManager.getAllJobRequestsForTag(WeatherDataUpdateJob.TAG).size() == 0)
            jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences, resources));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(resources.getString(R.string.preference_update_period_key))) {
        }
    }
}
