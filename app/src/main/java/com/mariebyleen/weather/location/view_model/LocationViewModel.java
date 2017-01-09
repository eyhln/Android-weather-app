package com.mariebyleen.weather.location.view_model;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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

    private final int NUM_SUGGESTIONS = 8;

    private WeatherLocation location;
    private LocationViewContract view;
    private GeoNamesApiService apiService;
    private Subscription locationTextViewSub;

    private String[] dropDownSuggestionsState = new String[NUM_SUGGESTIONS];

    @Inject
    public LocationViewModel(LocationViewContract view,
                             WeatherLocation location,
                             GeoNamesApiService apiService) {
        this.location = location;
        this.view = view;
        this.apiService = apiService;
    }

    public void setupSearchSuggestions(final AutoCompleteTextView locationTextView, final Activity activity,
                                       final Button selectButton) {
        selectButton.setEnabled(false);
        formatAutoCompleteTextView(locationTextView);
        locationTextViewSub = RxTextView.textChanges(locationTextView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map(new Func1<CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence) {
                        return charSequence.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        for(int i = 0; i < dropDownSuggestionsState.length; i++) {
                            if (s.equals(dropDownSuggestionsState[i])) {
                                selectButton.setEnabled(true);
                                return false;
                            }
                            else
                                selectButton.setEnabled(false);
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .switchMap(new Func1<String, Observable<SearchLocations>>() {
                    @Override
                    public Observable<SearchLocations> call(String s) {
                        return apiService.getSearchLocations(s, NUM_SUGGESTIONS);
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
                        String[] locations = mapLocationNames(searchLocations);
                        dropDownSuggestionsState = locations;
                        view.showLocationSuggestions(locations);
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
                String country = locations[i].getCountryName();
                names[i] = String.format("%s, %s, %s", name, admin, country);
            }
            return names;
        }
        return new String[0];
    }

    private void formatAutoCompleteTextView(AutoCompleteTextView textView) {
        textView.setThreshold(1);
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
        if (locationTextViewSub != null)
            locationTextViewSub.unsubscribe();
    }
}