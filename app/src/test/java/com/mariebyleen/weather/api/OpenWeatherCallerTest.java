package com.mariebyleen.weather.api;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.mariebyleen.weather.preferences.FakeSharedPreferences;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.mapper.WeatherMapper;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponse;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponseMain;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponseSys;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponseWeather;
import com.mariebyleen.weather.weather_display.model.current_conditions.CurrentConditionsResponseWind;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponse;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponseList;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponseListTemp;
import com.mariebyleen.weather.weather_display.model.forecast.ForecastResponseListWeather;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class OpenWeatherCallerTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    OpenWeatherApiService weatherApiService;
    @Mock
    Resources resources;
    @Mock
    Preferences mockPreferences;

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private WeatherMapper mapper;
    private Preferences preferences;

    private OpenWeatherCaller caller;

    @Before
    public void init() {
        sharedPreferences = new FakeSharedPreferences();
        gson = new Gson();
        mapper = new WeatherMapper();
        preferences = new Preferences(sharedPreferences, resources, gson);
        caller = new OpenWeatherCaller(mapper, weatherApiService, preferences);
    }

    @Test
    public void current_and_forecast_data_are_zipped() {
        when(weatherApiService.getCurrentConditions(anyFloat(), anyFloat(), anyString()))
                .thenReturn(getTestCurrentConditionsObservable(100.0));
        when(weatherApiService.getForecast(anyFloat(), anyFloat(), anyInt(), anyString()))
                .thenReturn(getTestForecastObservable("Test"));
        TestSubscriber<WeatherData> testSubscriber = new TestSubscriber<>();

        caller.getWeatherObservable(0.0f, 0.0f)
                .subscribe(testSubscriber);

        List<WeatherData> weatherDataEvents = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();

        WeatherData weatherData = weatherDataEvents.get(0);
        assertNotNull(weatherData);
        assertEquals(100.0, weatherData.getTemperature());
        assertEquals("Test", weatherData.getForecasts()[0].getDescription());

    }

        private Observable<CurrentConditionsResponse>
                                        getTestCurrentConditionsObservable(double temp) {
            CurrentConditionsResponse currentConditions = new CurrentConditionsResponse();
            currentConditions.setDt(0);
            currentConditions.setName("Name");
            CurrentConditionsResponseSys sys = new CurrentConditionsResponseSys();
            sys.setSunrise(0);
            sys.setSunset(0);
            currentConditions.setSys(sys);
            CurrentConditionsResponseMain main = new CurrentConditionsResponseMain();
            main.setTemp(temp);
            main.setHumidity(0.0);
            currentConditions.setMain(main);
            CurrentConditionsResponseWind wind = new CurrentConditionsResponseWind();
            wind.setSpeed(0.0);
            wind.setDeg(0.0);
            currentConditions.setWind(wind);
            CurrentConditionsResponseWeather weather = new CurrentConditionsResponseWeather();
            weather.setIcon("Icon");
            weather.setDescription("Description");
            CurrentConditionsResponseWeather[] weatherArray = new CurrentConditionsResponseWeather[1];
            weatherArray[0] = weather;
            currentConditions.setWeather(weatherArray);
            List<CurrentConditionsResponse> cc = new ArrayList<>(1);
            cc.add(currentConditions);
            return Observable.from(cc);
        }

        private Observable<ForecastResponse> getTestForecastObservable(String description) {
            int numEntries = 2;
            ForecastResponse forecast = new ForecastResponse();
            ForecastResponseList[] list = new ForecastResponseList[numEntries];
            for (int i = 0; i < numEntries; i++) {
                ForecastResponseList entry = new ForecastResponseList();
                entry.setDt(0);
                ForecastResponseListTemp temp = new ForecastResponseListTemp();
                temp.setMin(0.0);
                temp.setMax(0.0);
                entry.setTemp(temp);
                ForecastResponseListWeather[] weather = new ForecastResponseListWeather[1];
                ForecastResponseListWeather weatherEntry = new ForecastResponseListWeather();
                weatherEntry.setIcon("Icon");
                weatherEntry.setDescription(description);
                weather[0] = weatherEntry;
                entry.setWeather(weather);
                list[i] = entry;
            }
            forecast.setList(list);

            List<ForecastResponse> f = new ArrayList<>(1);
            f.add(forecast);
            return Observable.from(f);
        }

    @Test
    public void when_data_received_data_is_saved() {
        WeatherData saveWeatherData = new WeatherData();
        saveWeatherData.setTemperature(100.0);

        caller.saveData(saveWeatherData);

        WeatherData retrievedWeatherData = getWeatherDataFromPreferences();
        assertEquals(saveWeatherData.getTemperature(), retrievedWeatherData.getTemperature());
    }

    private WeatherData getWeatherDataFromPreferences() {
        String weatherJson = sharedPreferences.getString("WeatherData", "");
        return gson.fromJson(weatherJson, WeatherData.class);
    }
}
