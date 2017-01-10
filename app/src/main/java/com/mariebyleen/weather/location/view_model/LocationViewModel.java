package com.mariebyleen.weather.location.view_model;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocation;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocationViewModel extends BaseObservable {

    private final String TAG = "LocationViewModel";

    private final int NUM_SUGGESTIONS = 8;

    private WeatherLocation location;
    private LocationViewContract view;
    private GeoNamesApiService apiService;
    private Preferences preferences;
    private OpenWeatherCaller caller;

    private Subscription locationTextViewSub;
    private String[] dropDownSuggestionsState = new String[NUM_SUGGESTIONS];
    private AutoCompleteTextView searchLocationsTextView;

    private SearchLocations model;

    private float latitude;
    private float longitude;

    @Inject
    public LocationViewModel(LocationViewContract view,
                             WeatherLocation location,
                             GeoNamesApiService apiService,
                             Preferences preferences,
                             OpenWeatherCaller caller) {
        this.location = location;
        this.view = view;
        this.apiService = apiService;
        this.preferences = preferences;
        this.caller = caller;
    }

    public void setModel(SearchLocations searchLocations) {
        model = searchLocations;
    }

    public void setupSearchSuggestions(final AutoCompleteTextView searchLocationsTextView, final Activity activity,
                                       final Button selectButton) {
        this.searchLocationsTextView = searchLocationsTextView;
        selectButton.setEnabled(false);
        formatAutoCompleteTextView(searchLocationsTextView);
        locationTextViewSub = RxTextView.textChanges(searchLocationsTextView)
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
                        model = searchLocations;
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
                names[i] = mapLocationName(locations[i]);
            }
            return names;
        }
        return new String[0];
    }

    private String mapLocationName(SearchLocation location) {
        String name = location.getToponymName();
        String admin = location.getAdminName1();
        String country = location.getCountryName();
        return String.format("%s, %s, %s", name, admin, country);
    }

    private void formatAutoCompleteTextView(AutoCompleteTextView textView) {
        textView.setThreshold(1);
        textView.setDropDownWidth(900);
    }

    public void selectSearchLocation() {
        saveSearchLocationCoordinates();
        caller.getWeatherObservable(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "WeatherData data saved");
                        view.navigateToMainActivity(latitude, longitude);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        caller.saveData(weatherData);
                    }
                });
    }

    public void saveSearchLocationCoordinates() {
        SearchLocation[] locationSuggestions = model.getGeonames();
        String selectedLocationName = view.getSearchTextViewText();

        for (int i = 0; i < locationSuggestions.length; i++) {
            if (selectedLocationName.equals(mapLocationName(locationSuggestions[i]))) {
                saveCoordinates(i);
            }
        }
    }

    private void saveCoordinates(int index) {
        SearchLocation selectedLocation = model.getGeonames()[index];
        float lat = Float.parseFloat(selectedLocation.getLat());
        latitude = lat;
        float lon = Float.parseFloat(selectedLocation.getLng());
        longitude = lon;
        preferences.putCoordinates(lat, lon);
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