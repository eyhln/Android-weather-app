package com.mariebyleen.weather.update_timer;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class AutomaticUpdateTimer implements Observer<Long> {

    private final static String TAG = "UpdateTimer";

    private Observable<Long> observable;
    private long longState;

    private final Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

    public AutomaticUpdateTimer() {
        Observable<Long> counter = getCounterObservable();
        counter.subscribe(this);
        longState = -1L;
    }

    public Observable<Long> getCounterObservable() {
        observable = Observable.interval(10, TimeUnit.MINUTES, scheduler);
        return observable;
    }

    public Long getLastLong() {
        return longState;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void onCompleted() {
        Log.e(TAG, "Completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Error: " + e.toString());
    }

    @Override
    public void onNext(Long aLong) {
        longState = aLong.longValue();
        Log.i(TAG, aLong.toString());
    }
}
