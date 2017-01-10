package com.mariebyleen.weather.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import rx.Observer;

public class WeatherDataUpdateJob extends Job implements Observer<WeatherData> {

    public final static String TAG = "WeatherDataUpdateJob";

    private OpenWeatherCaller caller;
    private Preferences preferences;

    private float latitude;
    private float longitude;

    private boolean jobSuccess = false;

    public WeatherDataUpdateJob(OpenWeatherCaller caller,
                                Preferences preferences) {
        this.caller = caller;
        this.preferences = preferences;
    }

    public static JobRequest buildAutoUpdateJobRequest(Preferences preferences) {
        int period = preferences.getUpdatePeriod();
        return new JobRequest.Builder(TAG)
                .setPeriodic(period)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build();
    }

    public static JobRequest buildOneOffUpdateJobRequest() {
        return new JobRequest.Builder(TAG)
                .setExecutionWindow(2000, 3000)
                .build();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        getSavedCoordinates();
        caller.getWeatherObservable(latitude, longitude)
                .subscribe(this);
        return jobSuccess ? Result.SUCCESS : Result.FAILURE;
    }

    private void getSavedCoordinates() {
        latitude = preferences.getLatitude();
        longitude = preferences.getLongitude();
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "WeatherData data saved");
        jobSuccess = true;
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
        jobSuccess = false;
    }

    @Override
    public void onNext(WeatherData weatherData) {
        caller.saveData(weatherData);
    }
}