package com.mariebyleen.weather.job;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class WeatherDataUpdateJob extends Job implements Observer<CurrentConditions> {

    public final static String TAG = "WeatherDataUpdateJob";

    private OpenWeatherApiService weatherApiService;
    private CurrentConditionsMapper mapper;
    private Gson gson;
    private SharedPreferences preferences;

    public WeatherDataUpdateJob(OpenWeatherApiService weatherApiService,
                                CurrentConditionsMapper mapper,
                                Gson gson,
                                SharedPreferences preferences) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.gson = gson;
        this.preferences = preferences;
    }

    public static JobRequest buildJobRequest() {
        return new JobRequest.Builder("WeatherDataUpdateJob")
                .setExecutionWindow(500, 1000)
                .setPersisted(true)
                .build();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        weatherApiService.getCurrentConditions(getApiKey())
                .map(new Func1<CurrentConditionsResponse, CurrentConditions>() {
                    @Override
                    public CurrentConditions call(CurrentConditionsResponse currentConditionsResponse) {
                        return mapper.mapCurrentConditions(currentConditionsResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

        return Result.SUCCESS;
        // TODO return success only when the data is successfully transferred
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
    }

    @Override
    public void onNext(CurrentConditions currentConditions) {
        saveData(currentConditions);
    }

    private void saveData(CurrentConditions conditions) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);
        prefsEditor.apply();
    }
}
