package com.mariebyleen.weather.location.presenter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.api.OpenWeatherCaller;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.location.recent_locations.database.Database;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;
import com.mariebyleen.weather.location.search_locations.model.SearchLocation;
import com.mariebyleen.weather.location.search_locations.model.SearchLocations;
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

public class LocationPresenter {

    private final String TAG = "LocationPresenter";

    public static final int MAX_RECENT_LOCATIONS = 10;
    private final int NUM_SEARCH_SUGGESTIONS = 8;

    private WeatherLocation location;
    private LocationViewContract view;

    private Preferences preferences;
    private OpenWeatherCaller caller;
    private Database database;
    private RecentLocationsUpdater recentLocationsUpdater;
    private GeoNamesApiService apiService;
    private SelectLocationMapper mapper;

    private Subscription locationTextViewSub;
    private String[] dropDownSuggestionsState = new String[NUM_SEARCH_SUGGESTIONS];
    private AutoCompleteTextView searchLocationsTextView;
    private SearchLocations searchLocations;

    private RecentLocations recentLocations;

    @Inject
    public LocationPresenter(LocationViewContract view,
                             WeatherLocation location,
                             Preferences preferences,
                             GeoNamesApiService apiService,
                             OpenWeatherCaller caller,
                             Database database,
                             RecentLocationsUpdater recentLocationsUpdater,
                             SelectLocationMapper mapper) {
        this.location = location;
        this.view = view;
        this.apiService = apiService;
        this.preferences = preferences;
        this.caller = caller;
        this.database = database;
        this.recentLocationsUpdater = recentLocationsUpdater;
        this.mapper = mapper;
    }

    public void onViewResume() {
        if (recentLocations == null) {
            recentLocations = database.getRecentLocations();
        }
    }

    // Load search locations
    // --------------------------------------------------------------------------------

    public void setSearchLocations(SearchLocations searchLocations) {
        this.searchLocations = searchLocations;
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
                        return apiService.getSearchLocations(s, NUM_SEARCH_SUGGESTIONS);
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
                        LocationPresenter.this.searchLocations = searchLocations;
                        String[] locations = mapper.mapSearchLocationNames(searchLocations);
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

    private void formatAutoCompleteTextView(AutoCompleteTextView textView) {
        textView.setThreshold(1);
        textView.setDropDownWidth(900);
    }

    // Load recent locations
    // --------------------------------------------------------------------------------

    public String[] getRecentLocationNames() {
        if (recentLocations != null) {
            return getLocationNames(recentLocations);
        }
        return new String[0];
    }

    public String[] getLocationNames(RecentLocations recentLocations) {
        List<RecentLocation> locations = recentLocations.getRecentLocations();
        List<String> names = new ArrayList<>();
        for (int i = 0; i< locations.size(); i++) {
            RecentLocation location = locations.get(i);
            if (location != null && location.getName() != null)
                names.add(location.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    // Select a search location
    // --------------------------------------------------------------------------------

    public void selectSearchLocation() {
        SearchLocation selectedLocation = saveLocationCoordinates();
        RecentLocation location = mapper.mapSearchLocationToRecentLocation(selectedLocation);
        saveLocationCoordinates(location);
        updateAndSaveRecentLocationsList(location);
        //updateWeatherDataAndReturn(location);
    }

    private void updateAndSaveRecentLocationsList(RecentLocation location) {
        recentLocations = recentLocationsUpdater.updateRecentLocations(recentLocations, location);
        database.insertRecentLocations(recentLocations);
    }

    @Nullable
    private SearchLocation saveLocationCoordinates() {
        SearchLocation selectedLocation = null;
        if (searchLocations != null) {
            SearchLocation[] locationSuggestions = searchLocations.getGeonames();
            String selectedLocationName = view.getSearchTextViewText();

            for (int i = 0; i < locationSuggestions.length; i++) {
                if (selectedLocationName.equals(mapper.mapLocationName(locationSuggestions[i]))) {
                    selectedLocation = searchLocations.getGeonames()[i];
                }
            }
        }
        return selectedLocation;
    }

    private void saveLocationCoordinates(RecentLocation location) {
        float lat = location.getLat();
        float lon = location.getLon();
        preferences.putCoordinates(lat, lon);
    }

    // Select a recent location
    // --------------------------------------------------------------------------------

    public void selectRecentLocation(String selection) {
        if (selection != null) {
            RecentLocation location = mapSelectionNameToRecentLocation(selection);
            recentLocations = recentLocationsUpdater.updateRecentLocations(recentLocations, location);
            database.insertRecentLocations(recentLocations);
            updateWeatherDataAndReturn(location);
        }
    }

    @Nullable
    private RecentLocation mapSelectionNameToRecentLocation(String selection) {
        RecentLocation location = null;
        if (recentLocations != null) {
            List<RecentLocation> locations = recentLocations.getRecentLocations();
            for (int i = 0; i < locations.size(); i++) {
                RecentLocation currLocation = recentLocations.getRecentLocations().get(i);
                if (selection.equals(location.getName())) {
                    location = currLocation;
                }
            }
        }
        return location;
    }

    // Update weather data and return to main activity
    // --------------------------------------------------------------------------------

    public void updateWeatherDataAndReturn(RecentLocation location) {
        float latitude = location.getLat();
        float longitude = location.getLon();
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

    // --------------------------------------------------------------------------------

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