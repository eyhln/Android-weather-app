package com.mariebyleen.weather.application.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.mariebyleen.weather.api.OpenWeatherApiService;
import com.mariebyleen.weather.job.WeatherJobCreator;
import com.mariebyleen.weather.navigation.Navigator;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private Context context;
    private String baseUrl;
    private Application application;

    public ApplicationModule(Context context, String baseUrl, Application application) {
        this.context = context;
        this.baseUrl = baseUrl;
        this.application = application;
    }

    @Singleton
    @Provides
    JobManager provideJobManager(JobCreator jobCreator) {
        JobManager.create(application).addJobCreator(jobCreator);
        return JobManager.instance();
    }

    @Singleton
    @Provides
    JobCreator provideJobCreator() {
        return new WeatherJobCreator();
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    Resources provideResources(Context context) {
        return context.getResources();
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
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

    @Singleton
    @Provides
    Navigator provideNavigator() {
        return new Navigator();
    }
}
