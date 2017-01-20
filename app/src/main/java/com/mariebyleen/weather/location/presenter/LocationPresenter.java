package com.mariebyleen.weather.location.presenter;

import android.databinding.BaseObservable;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocation;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.recent_locations.database.Database;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;
import com.mariebyleen.weather.preferences.Preferences;
import com.mariebyleen.weather.weather_display.model.mapped.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocationPresenter extends BaseObservable {

    private final String TAG = "LocationPresenter";

    private final int NUM_SUGGESTIONS = 8;

    private WeatherLocation location;
    private LocationViewContract view;
    private GeoNamesApiService apiService;
    private Preferences preferences;
    private OpenWeatherCaller caller;
    private Database database;

    private Subscription locationTextViewSub;
    private String[] dropDownSuggestionsState = new String[NUM_SUGGESTIONS];
    private AutoCompleteTextView searchLocationsTextView;

    private SearchLocations model;

    private RecentLocations recentLocations;

    @Inject
    public LocationPresenter(LocationViewContract view,
                             WeatherLocation location,
                             GeoNamesApiService apiService,
                             Preferences preferences,
                             OpenWeatherCaller caller,
                             Database database) {
        this.location = location;
        this.view = view;
        this.apiService = apiService;
        this.preferences = preferences;
        this.caller = caller;
        this.database = database;
    }

    public void onViewResume() {
        if (recentLocations == null) {
            recentLocations = database.getRecentLocations();
        }
    }

    public void setModel(SearchLocations searchLocations) {
        model = searchLocations;
    }

    public void setupSearchSuggestions(final AutoCompleteTextView searchLocationsTextView) {
        this.searchLocationsTextView = searchLocationsTextView;
        formatAutoCompleteTextView(searchLocationsTextView);
        view.enableSearchLocationSelection(false);

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
                        boolean isValid = inputIsValid(s);
                        view.enableSearchLocationSelection(isValid);
                        return !isValid;
                    }
                })
                .observeOn(Schedulers.newThread())
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
                        Log.d(TAG, "Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        view.showNoAccessToWeatherServiceErrorMessage();
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

    private boolean inputIsValid(String s) {
        if (dropDownSuggestionsState == null)
            return false;

        for(int i = 0; i < dropDownSuggestionsState.length; i++) {
            if (s.equals(dropDownSuggestionsState[i]))
                return true;
        }
        return false;
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
        /*
        RecentLocation selectedLocation = saveSearchLocationCoordinates();
        database.insertRecentLocation(selectedLocation);
        updateWeatherData(selectedLocation.getLat(), selectedLocation.getLon());
        */
    }

    protected RecentLocation saveSearchLocationCoordinates() {
        String name = null;
        float lat = 0f;
        float lon = 0f;
        if (model != null) {
            SearchLocation[] locationSuggestions = model.getGeonames();
            String selectedLocationName = view.getSearchTextViewText();
            name = selectedLocationName;

            for (int i = 0; i < locationSuggestions.length; i++) {
                if (selectedLocationName.equals(mapLocationName(locationSuggestions[i]))) {
                    SearchLocation selectedLocation = model.getGeonames()[i];
                    lat = Float.parseFloat(selectedLocation.getLat());
                    lon = Float.parseFloat(selectedLocation.getLng());
                    preferences.putCoordinates(lat, lon);
                }
            }
        }
        return new RecentLocation(name, lat, lon, 0);
    }

    public String[] getRecentLocationNames() {
        if (recentLocations != null) {
            return getLocationsWithNullEntriesRemoved(recentLocations.getRecentLocations());
        }
        return new String[0];
    }

    private String[] getLocationsWithNullEntriesRemoved(RecentLocation[] recentLocations) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i< recentLocations.length; i++) {
            if (recentLocations[i] != null && recentLocations[i].getName() != null)
                names.add(recentLocations[i].getName());
        }
        return names.toArray(new String[names.size()]);
    }

    public void selectRecentLocation(String selection) {
        if (recentLocations != null) {
            for (int i = 0; i< recentLocations.getRecentLocations().length; i++) {
                String name = recentLocations.getRecentLocations()[i].getName();
                float lat = recentLocations.getRecentLocations()[i].getLat();
                float lon = recentLocations.getRecentLocations()[i].getLon();
                if (selection.equals(name))
                    // Save recent location as most recent location
                    updateWeatherData(lat,lon);
            }
        }
    }

    private void updateWeatherData(float latitude, float longitude) {
        caller.getWeatherObservable(latitude, longitude)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherData>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "WeatherData data saved");
                        view.navigateToMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error retrieving weather data: \n" + e.toString());
                        view.showNoAccessToWeatherServiceErrorMessage();
                        view.enableUseRecentLocationSelection(false);
                    }

                    @Override
                    public void onNext(WeatherData weatherData) {
                        caller.saveData(weatherData);
                    }
                });
    }

    public void onViewPause() {
        database.insertRecentLocations(recentLocations);
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