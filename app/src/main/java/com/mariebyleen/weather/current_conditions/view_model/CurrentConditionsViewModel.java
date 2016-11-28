package com.mariebyleen.weather.current_conditions.view_model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.mariebyleen.weather.current_conditions.mapper.CurrentConditionsMapper;
import com.mariebyleen.weather.current_conditions.model.CurrentConditions;
import com.mariebyleen.weather.current_conditions.model.CurrentConditionsResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class CurrentConditionsViewModel extends BaseObservable
        implements Observer<CurrentConditionsResponse> {

    private static final String TAG = "CurrentConditionsVM";
    private double lat = 0;
    private double lon = 0;

    private UpdateService updates;
    private CurrentConditionsMapper mapper;

    private CurrentConditions conditions;
    private Subscription subscription;
    private Observable<CurrentConditionsResponse> observable;

    @Inject
    public CurrentConditionsViewModel(UpdateService updateService,
                                      CurrentConditionsMapper mapper) {
        this.updates = updateService;
        this.mapper = mapper;
    }

    public void onFragmentResume() {
        if (conditions == null) {
            Log.d(TAG, "populating data from memory");
            conditions = updates.getSavedConditions();
        }

        if (updates.missedMostRecentUpdate()) {
            updates.getManualUpdateObservable()
                    .subscribe(this);
            Log.d(TAG, "refreshing weather data manually");
        }

        observable = updates.getAutomaticUpdateObservable();
        startUpdates();
    }

    public void onFragmentPause() {
        stopUpdates();
        updates.saveData(conditions);
    }

    private void startUpdates() {
        subscription = observable.subscribe(this);
        Log.d(TAG, "Subscribing to observable: " + observable.toString());
    }

    private void stopUpdates() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
            Log.d(TAG, "un-subscribing");
        }
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "Current conditions weather data update successfully completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error retrieving current conditions weather data: \n" + e.toString());
    }

    @Override
    public void onNext(CurrentConditionsResponse currentConditionsResponse) {
        Log.d(TAG, "onNext called");
        conditions = mapper.mapCurrentConditions(currentConditionsResponse);
        notifyChange();
    }

    @Bindable
    public String getTemperature() {
        double temperature = conditions.getTemperature();
        Log.d(TAG, "getTemperature called");
        return "Temperature: " + String.valueOf(temperature);
    }

}
