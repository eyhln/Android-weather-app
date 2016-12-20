package com.mariebyleen.weather.api;

import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherApiService {

    @GET("weather")
    Observable<CurrentConditionsResponse> getCurrentConditions(@Query("lat") float lat,
                                                               @Query("lon") float lon,
                                                               @Query("appid") String apiKey);

    @GET("forecast/daily")
    Observable<ForecastResponse> getForecast(@Query("lat") float lat,
                                             @Query("lon") float lon,
                                             @Query("cnt") int count,
                                             @Query("appid") String apiKey);

    @GET("find")
    Observable<CurrentConditionsResponse> getLocation(@Query("q") String searchString,
                                                      @Query("type") String mode,
                                                      @Query("cnt") int numResults,
                                                      @Query("appid") String apiKey);
}
