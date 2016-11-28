package com.mariebyleen.weather.application.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mariebyleen.weather.update_timer.AutomaticUpdateTimer;
import com.mariebyleen.weather.api.OpenWeatherApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

@Module
public class ApplicationModule {

    private Context context;
    private String baseUrl;

    public ApplicationModule(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    AutomaticUpdateTimer provideAutomaticUpdateTimer() {
        return new AutomaticUpdateTimer();
    }

    @Singleton
    @Provides
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    @Singleton
    @Provides
    GoogleApiAvailability provideGoogleApiAvailability() {
        return GoogleApiAvailability.getInstance();
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences("com.mariebyleen.weather", MODE_PRIVATE);
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        return httpClient.build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory converterFactory,
                             RxJavaCallAdapterFactory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    OpenWeatherApiService provideOpenWeatherApiService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherApiService.class);
    }
}
