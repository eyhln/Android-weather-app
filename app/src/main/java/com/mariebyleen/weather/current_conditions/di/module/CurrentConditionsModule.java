package com.mariebyleen.weather.current_conditions.di.module;

import android.content.SharedPreferences;

import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.application.di.scope.PerActivity;
import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.current_conditions.view_model.UpdateService;
import com.mariebyleen.weather.update_timer.AutomaticUpdateTimer;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module
public class CurrentConditionsModule {

    @PerActivity
    @Provides
    CurrentConditionsViewModel provideCurrentConditionsViewModel(CurrentConditionsMapper mapper,
                                                                 UpdateService updateService,
                                                                 JobRequest.Builder jobScheduler) {
        return new CurrentConditionsViewModel(updateService, mapper, jobScheduler);
    }

    @PerActivity
    @Provides
    CurrentConditionsMapper provideCurrentConditionsMapper() {
        return new CurrentConditionsMapper();
    }

    @PerActivity
    @Provides
    UpdateService provideUpdateService(SharedPreferences preferences,
                               Gson gson,
                               AutomaticUpdateTimer timer,
                               OpenWeatherApiService weatherApiService) {
        return new UpdateService(preferences, gson, timer, weatherApiService);
    }

    @PerActivity
    @Provides
    JobRequest.Builder provideJobRequestBuilder() {
        return new JobRequest.Builder("WeatherDataUpdateJob");
    }
}
