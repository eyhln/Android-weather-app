package com.mariebyleen.weather.api;

import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponse;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

import static com.mariebyleen.weather.application.WeatherApplication.getApiKey;

public class OpenWeatherCaller {

    private WeatherMapper mapper;
    private OpenWeatherApiService weatherApiService;
    private Preferences preferences;

    @Inject
    public OpenWeatherCaller(WeatherMapper mapper,
                             OpenWeatherApiService weatherApiService,
                             Preferences preferences) {
        this.mapper = mapper;
        this.weatherApiService = weatherApiService;
        this.preferences = preferences;
    }

    public Observable<WeatherData> getWeatherObservable(float lat, float lon) {
        Observable<CurrentConditionsResponse> conditions =
                weatherApiService.getCurrentConditions(lat, lon, getApiKey());
        Observable<ForecastResponse> forecast =
                weatherApiService.getForecast(lat, lon, 16, getApiKey());

        return conditions.zipWith(forecast, new
                Func2<CurrentConditionsResponse, ForecastResponse, WeatherData>() {
                    @Override
                    public WeatherData call(CurrentConditionsResponse ccResponse,
                                            ForecastResponse fResponse) {
                        return mapper.map(ccResponse, fResponse);
                    }
                });
    }

    public void saveData(WeatherData weatherData) {
        weatherData.setUpdateTime(System.currentTimeMillis() / 1000);
        preferences.putWeatherData(weatherData);
    }
}
