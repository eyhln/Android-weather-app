package com.mariebyleen.weather.job;


import android.content.SharedPreferences;
import android.content.res.Resources;

import com.evernote.android.job.JobManager;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.preferences.Preferences;

import javax.inject.Inject;

public class WeatherDataService implements SharedPreferences.OnSharedPreferenceChangeListener {

    private JobManager jobManager;
    private Resources resources;
    private Preferences preferences;

    private boolean running;

    @Inject
    public WeatherDataService(JobManager jobManager, Resources resources, Preferences preferences) {
        this.jobManager = jobManager;
        this.resources = resources;
        this.preferences = preferences;
        running = false;
    }

    public void manageJobRequests() {
        if (running == false) {
            scheduleOneOffUpdate();
            scheduleAutoUpdateJobs();
        }
        running = true;
    }

    public void scheduleOneOffUpdate() {
        jobManager.schedule(WeatherDataUpdateJob.buildOneOffUpdateJobRequest());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(resources.getString(R.string.preference_update_period_key))) {
            manageJobRequests();
        }
    }

    private void scheduleAutoUpdateJobs() {
        jobManager.schedule(WeatherDataUpdateJob.buildAutoUpdateJobRequest(preferences));
    }
}
