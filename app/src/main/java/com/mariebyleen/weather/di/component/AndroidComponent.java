package com.mariebyleen.weather.di.component;

import android.content.Context;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mariebyleen.weather.di.module.AndroidModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AndroidModule.class)
public interface AndroidComponent {

    GoogleApiClient exposeGoogleApiClient();
    GoogleApiAvailability exposeGoogleApiAvailability();
    Context exposeContext();
}
