package com.mariebyleen.weather.location.di.module;

import android.content.Context;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mariebyleen.weather.di.scope.PerActivity;
import com.mariebyleen.weather.location.model.CurrentLocation;
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
    LocationPresenterContract provideLocationPresenterContract(CurrentLocation finder) {
        return new LocationPresenter(view, finder);
    }

    @PerActivity
    @Provides
    CurrentLocation provideCurrentLocationFinder(GoogleApiClient client, Context context,
                                                 GoogleApiAvailability availability) {
        return new CurrentLocation(client, context, availability);
    }

    @PerActivity
    @Provides
    LocationViewContract provideLocationViewContract() {
        return view;
    }

}
