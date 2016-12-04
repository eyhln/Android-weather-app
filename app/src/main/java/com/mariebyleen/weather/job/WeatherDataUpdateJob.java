package com.mariebyleen.weather.job;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import rx.Observer;
import rx.functions.Func1;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class WeatherDataUpdateJob extends Job implements Observer<CurrentConditions> {

    public final static String TAG = "WeatherDataUpdateJob";

    private OpenWeatherApiService weatherApiService;
    private CurrentConditionsMapper mapper;
    private SharedPreferences preferences;
    private Gson gson;
    private SharedPreferences.Editor prefsEditor;

    public WeatherDataUpdateJob(OpenWeatherApiService weatherApiService,
                                CurrentConditionsMapper mapper,
                                SharedPreferences preferences,
                                Gson gson) {
        this.weatherApiService = weatherApiService;
        this.mapper = mapper;
        this.preferences = preferences;
        this.gson = gson;
        prefsEditor = preferences.edit();
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
                .subscribe(this);

        return Result.SUCCESS;
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Current conditions weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving current conditions weather data: \n" + e.toString());
    }

    @Override
    public void onNext(CurrentConditions currentConditions) {
        saveData(currentConditions);
    }

    private void saveData(CurrentConditions conditions) {
        String currentConditionsJson = gson.toJson(conditions);
        prefsEditor.putString("CurrentConditions", currentConditionsJson);
        prefsEditor.apply();
    }
}
