package com.mariebyleen.weather.job;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.forecast.model.ForecastResponse;
import com.mariebyleen.weather.mapper.WeatherMapper;
import com.mariebyleen.weather.model.Weather;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class WeatherDataUpdateJob extends Job implements Observer<Weather> {

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
                .build();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        getWeatherObservable()
                .subscribe(this);
        return jobSuccess ? Result.SUCCESS : Result.FAILURE;
    }


    Observable<Weather> getWeatherObservable() {
        float latitude = preferences.getFloat("lat", 0);
        float longitude = preferences.getFloat("lon", 0);
        Observable<CurrentConditionsResponse> conditions =
                weatherApiService.getCurrentConditions(latitude, longitude, getApiKey());
        Observable<ForecastResponse> forecast =
                weatherApiService.getForecast(latitude, longitude, getApiKey());

        return conditions.zipWith(forecast, new
                Func2<CurrentConditionsResponse, ForecastResponse, Weather>() {
                    @Override
                    public Weather call(CurrentConditionsResponse ccResponse,
                                        ForecastResponse fResponse) {
                        return mapper.map(ccResponse, fResponse);
                    }
                });
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Weather data saved");
        jobSuccess = true;
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
        jobSuccess = false;
    }

    @Override
    public void onNext(Weather weather) {
        saveData(weather);
    }

    private void saveData(Weather weather) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String currentConditionsJson = gson.toJson(weather);
        prefsEditor.putString("Weather", currentConditionsJson);
        prefsEditor.apply();
    }

}