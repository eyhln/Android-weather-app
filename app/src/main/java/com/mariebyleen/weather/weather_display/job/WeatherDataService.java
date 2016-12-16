package com.mariebyleen.weather.weather_display.job;


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

    public void onFirstRunScheduleDefaultJobs() {
        if (isFirstRun()) {
            scheduleUpdateJobs();
            setFirstRunFalse();
        }
    }

    private boolean isFirstRun() {
        firstRunKey = resources.getString(R.string.preference_first_run_key);
        return preferences.getBoolean(firstRunKey, true);
    }

    private void setFirstRunFalse() {
        preferences.edit().putBoolean(firstRunKey, false).apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(resources.getString(R.string.preference_update_period_key))) {
            scheduleUpdateJobs();
        }
    }

    private void scheduleUpdateJobs() {
        jobManager.schedule(WeatherDataUpdateJob.buildJobRequest(preferences, resources));
    }
}
