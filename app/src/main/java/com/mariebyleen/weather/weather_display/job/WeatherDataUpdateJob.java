package com.mariebyleen.weather.weather_display.job;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.model.WeatherData;
import com.mariebyleen.weather.weather_display.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.forecast.model.ForecastResponse;

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
    private Resources resources;

    private boolean jobSuccess = false;

    public WeatherDataUpdateJob(OpenWeatherApiService weatherApiService,
                                WeatherMapper mapper,
                                Gson gson,
                                SharedPreferences preferences,
                                Resources resources) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.gson = gson;
        this.preferences = preferences;
        this.resources = resources;
    }

    public static JobRequest buildAutoUpdateJobRequest(SharedPreferences preferences, Resources resources) {
        String periodString = preferences.getString(
                resources.getString(R.string.preference_update_period_key), "900000");
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
        // TODO remove these default values
        float latitude = preferences.getFloat("lat",  38.9716700f);
        float longitude = preferences.getFloat("lon",  -95.2352500f);
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
        weatherData.setUpdateTime(System.currentTimeMillis() / 1000);
        String currentConditionsJson = gson.toJson(weatherData);
        prefsEditor.putString(resources.getString(R.string.preference_weather_data_key),
                currentConditionsJson);
        prefsEditor.apply();
    }

}