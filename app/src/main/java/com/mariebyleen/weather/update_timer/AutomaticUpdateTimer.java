package com.mariebyleen.weather.update_timer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class AutomaticUpdateTimer implements Observer<Long> {

    private final static String TAG = "UpdateTimer";

    private Observable<Long> observable;
    private boolean needsUpdate = true;

    private final Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

    public AutomaticUpdateTimer() {
        observable = Observable.interval(30, TimeUnit.SECONDS, scheduler);
        observable.subscribe(this);
    }

    public Observable<Long> getCounterObservable() {
        return observable;
    }

    public boolean needsManualUpdate() {
        return needsUpdate;
    }

    public void notifyUpdated() {
        //Log.d(TAG, "set needsUpdate to false");
        needsUpdate = false;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void onCompleted() {
        //Log.e(TAG, "Completed");
    }

    @Override
    public void onError(Throwable e) {
        //Log.e(TAG, "Error: " + e.toString());
    }

    @Override
    public void onNext(Long aLong) {
        needsUpdate = true;
        //Log.d(TAG, "set needsUpdate to true");
        //Log.i(TAG, aLong.toString());
    }
}
