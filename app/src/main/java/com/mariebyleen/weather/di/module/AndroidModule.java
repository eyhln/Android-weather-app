package com.mariebyleen.weather.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {

    private Context context;

    public AndroidModule(Context context) {
        this.context = context;
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
    SharedPreferences provideSharePreferences() {
        return null;
    }

}
