package com.mariebyleen.weather.api;

import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherApiService {

    @GET("weather?lat=38.9716700&lon=-95.2352500")
    Observable<CurrentConditionsResponse> getCurrentConditions(@Query("APPID") String apiKey);
}
