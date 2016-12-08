package com.mariebyleen.weather.api;

import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;
import com.mariebyleen.weather.forecast.model.ForecastResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherApiService {

    @GET("weather")
    Observable<CurrentConditionsResponse> getCurrentConditions(@Query("lat") float lat,
                                                               @Query("lon") float lon,
                                                               @Query("appid") String apiKey);

    @GET("forecast")
    Observable<ForecastResponse> getForecast(@Query("lat") float lat,
                                             @Query("lon") float lon,
                                             @Query("appid") String apiKey);
}
