package com.mariebyleen.weather.location.di.module;

import android.content.Context;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mariebyleen.weather.di.scope.PerActivity;
import com.mariebyleen.weather.location.CurrentLocationFinder;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;
import com.mariebyleen.weather.location.view.LocationPresenterContract;

import dagger.Module;
import dagger.Provides;

@Module
@PerActivity
public class LocationModule {

    private LocationViewContract view;
    private Context context;

    public LocationModule(LocationViewContract view, Context context) {
        this.view = view;
        this.context = context;
    }

    @PerActivity
    @Provides
    LocationPresenterContract provideLocationPresenterContract(CurrentLocationFinder finder) {
        return new LocationPresenter(view, finder);
    }

    @PerActivity
    @Provides
    CurrentLocationFinder provideCurrentLocationFinder(GoogleApiClient client, Context context,
                                                       GoogleApiAvailability availability) {
        return new CurrentLocationFinder(client, context, availability);
    }

    @PerActivity
    @Provides
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
    }

    @PerActivity
    @Provides
    GoogleApiAvailability provideGoogleApiAvailability() {
        return GoogleApiAvailability.getInstance();
    }

    @PerActivity
    @Provides
    LocationViewContract provideLocationViewContract() {
        return view;
    }

    @PerActivity
    @Provides
    Context provideContext() {
        return context;
    }


}
