package com.mariebyleen.weather.job;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;
import com.mariebyleen.weather.mapper.WeatherMapper;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class WeatherDataUpdateJob extends Job implements Observer<WeatherData> {

    public final static String TAG = "WeatherDataUpdateJob";

    private OpenWeatherApiService weatherApiService;
    private WeatherMapper mapper;
    private Gson gson;
    private SharedPreferences preferences;

    private boolean jobSuccess = false;

    public WeatherDataUpdateJob(OpenWeatherApiService weatherApiService,
                                WeatherMapper mapper,
                                Gson gson,
                                SharedPreferences preferences) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.gson = gson;
        this.preferences = preferences;
    }

    public static JobRequest buildJobRequest(SharedPreferences preferences) {
        String periodString = preferences.getString("UPDATE_PERIOD", "900000");
        int period = Integer.parseInt(periodString);
        return new JobRequest.Builder("WeatherDataUpdateJob")
                .setPeriodic(period)
                .setPersisted(true)
                .setUpdateCurrent(true)
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
        // TODO remove these default values
        float latitude = preferences.getFloat("lat",  38.9716700f);
        float longitude = preferences.getFloat("lon",  -95.2352500f);
        Observable<CurrentConditionsResponse> conditions =
                weatherApiService.getCurrentConditions(latitude, longitude, getApiKey());
        Observable<ForecastResponse> forecast =
                weatherApiService.getForecast(latitude, longitude, getApiKey());

        return conditions.zipWith(forecast, new
                Func2<CurrentConditionsResponse, ForecastResponse, WeatherData>() {
                    @Override
                    public WeatherData call(CurrentConditionsResponse ccResponse,
                                            ForecastResponse fResponse) {
                        return mapper.map(ccResponse, fResponse);
                    }
                });
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
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String currentConditionsJson = gson.toJson(weatherData);
        prefsEditor.putString("WeatherData", currentConditionsJson);
        prefsEditor.apply();
    }

}