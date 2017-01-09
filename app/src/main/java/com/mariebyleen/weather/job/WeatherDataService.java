package com.mariebyleen.weather.job;


import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.R;

import javax.inject.Inject;

public class WeatherDataService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private JobManager jobManager;
    private SharedPreferences preferences;
    private Resources resources;

    private String firstRunKey;

    @Inject
    public WeatherDataService(JobManager jobManager, SharedPreferences preferences,
                              Resources resources) {
        this.jobManager = jobManager;
        this.preferences = preferences;
        this.resources = resources;
    }

    public void manageJobRequests() {
        if (jobManager.getAllJobRequests().size() == 0) {
            scheduleAutoUpdateJobs();
        }
    }

    public void scheduleOneOffUpdate() {
        jobManager.schedule(WeatherDataUpdateJob.buildOneOffUpdateJobRequest());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(resources.getString(R.string.preference_update_period_key))) {
            scheduleAutoUpdateJobs();
        }
    }

    private void scheduleAutoUpdateJobs() {
        jobManager.schedule(WeatherDataUpdateJob.buildAutoUpdateJobRequest(preferences, resources));
    }
}
