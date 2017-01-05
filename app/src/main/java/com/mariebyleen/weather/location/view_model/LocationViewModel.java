package com.mariebyleen.weather.location.view_model;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocation;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;
import com.mariebyleen.weather.location.model.WeatherLocation;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocationViewModel extends BaseObservable {

    private WeatherLocation location;
    private LocationViewContract view;
    private GeoNamesApiService apiService;
    private Subscription editLocationFieldSub;

    @Inject
    public LocationViewModel(LocationViewContract view,
                             WeatherLocation location,
                             GeoNamesApiService apiService) {
        this.location = location;
        this.view = view;
        this.apiService = apiService;
    }

    public void onViewResume(final AutoCompleteTextView enterLocationField, final Activity activity) {
        formatDropdownMenu(enterLocationField);
        editLocationFieldSub = RxTextView.textChanges(enterLocationField)
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                            return charSequence.toString();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(new Func1<String, Observable<SearchLocations>>() {
                    @Override
                    public Observable<SearchLocations> call(String s) {
                        Observable<SearchLocations> locations = apiService.getSearchLocations(s);
                        Log.d("RX", locations.toString());
                            return locations;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchLocations>() {
                    @Override
                    public void onCompleted() {
                        Log.d("RX", "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SearchLocations searchLocations) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    activity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    mapLocationNames(searchLocations));
                            enterLocationField.setAdapter(adapter);
                            enterLocationField.showDropDown();
                        }
                    });
    }

    private String[] mapLocationNames(SearchLocations searchLocations) {
        SearchLocation[] locations = searchLocations.getGeonames();
        if (locations != null && locations.length > 0) {
            String[] names = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                String name = locations[i].getToponymName();
                String admin = locations[i].getAdminName1();
                String country = locations[i].getCountryCode();
                names[i] = String.format("%s, %s, %s", name, admin, country);
            }
            return names;
        }
        return new String[0];
    }

    private void formatDropdownMenu(AutoCompleteTextView textView) {
        textView.setDropDownWidth(900);
    }


    public void useCurrentLocation() {
        view.checkPermissions();
    }

    public void onLocationPermissionGranted() {
        location.updateLocationData();
    }

    public void onLocationPermissionDenied() {
        view.disableUseCurrentLocationOption();
    }

    public void onViewStop() {
        if (editLocationFieldSub != null)
            editLocationFieldSub.unsubscribe();
    }
}