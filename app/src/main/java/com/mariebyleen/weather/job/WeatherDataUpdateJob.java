package com.mariebyleen.weather.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponse;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class WeatherDataUpdateJob extends Job implements Observer<WeatherData> {

    public final static String TAG = "WeatherDataUpdateJob";

    private OpenWeatherApiService weatherApiService;
    private WeatherMapper mapper;
    private Preferences preferences;

    private float latitude;
    private float longitude;

    private boolean jobSuccess = false;

    public WeatherDataUpdateJob(OpenWeatherApiService weatherApiService,
                                WeatherMapper mapper,
                                Preferences preferences) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.preferences = preferences;

        getSavedCoordinates();
    }

    public static JobRequest buildAutoUpdateJobRequest(Preferences preferences) {
        String periodString = preferences.getString(R.string.preference_update_period_key, "900000");
        int period = Integer.parseInt(periodString);
        return new JobRequest.Builder(TAG)
                .setPeriodic(period)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build();
    }

    public static JobRequest buildOneOffUpdateJobRequest() {
        return new JobRequest.Builder(TAG)
                .setExecutionWindow(1, 10)
                .build();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        getWeatherObservable()
                .subscribe(this);
        return jobSuccess ? Result.SUCCESS : Result.FAILURE;
    }


    protected Observable<WeatherData> getWeatherObservable() {
        Observable<CurrentConditionsResponse> conditions =
                weatherApiService.getCurrentConditions(latitude, longitude, getApiKey());
        Observable<ForecastResponse> forecast =
                weatherApiService.getForecast(latitude, longitude, 16, getApiKey());

        return conditions.zipWith(forecast, new
                Func2<CurrentConditionsResponse, ForecastResponse, WeatherData>() {
                    @Override
                    public WeatherData call(CurrentConditionsResponse ccResponse,
                                            ForecastResponse fResponse) {
                        return mapper.map(ccResponse, fResponse);
                    }
                });
    }

    private void getSavedCoordinates() {
        latitude = preferences.getFloat(R.string.preference_latitude_key,  0.00f);
        longitude = preferences.getFloat(R.string.preference_longitude_key,  0.00f);
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
        saveData(weatherData);
    }

    private void saveData(WeatherData weatherData) {
        weatherData.setUpdateTime(System.currentTimeMillis() / 1000);
        preferences.putWeatherData(weatherData);
    }
}