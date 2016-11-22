package com.mariebyleen.weather.location.di.module;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mariebyleen.weather.application.di.scope.PerActivity;
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
                                                 GoogleApiAvailability availability,
                                                 LocationManager manager, Criteria criteria) {
        return new CurrentLocation(client, context, availability, manager, criteria);
    }

    @PerActivity
    @Provides
    LocationViewContract provideLocationViewContract() {
        return view;
    }

    @PerActivity
    @Provides
    LocationManager provideLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @PerActivity
    @Provides
    Criteria provideCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setCostAllowed(false);
        return criteria;
    }

}
