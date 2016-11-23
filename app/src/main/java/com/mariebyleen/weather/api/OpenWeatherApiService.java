package com.mariebyleen.weather.api;

import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherApiService {

    @GET("weather")
    Observable<CurrentConditionsResponse> getCurrentConditions(
            @Query("lat") double lat, @Query("lon") double lon);
}
