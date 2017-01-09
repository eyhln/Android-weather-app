package com.mariebyleen.weather.location.view_model;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.AutoCompleteTextView;

import com.mariebyleen.weather.FakeSharedPreferences;
import com.mariebyleen.weather.R;
import com.mariebyleen.weather.api.GeoNamesApiService;
import com.mariebyleen.weather.job.WeatherDataService;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocation;
import com.mariebyleen.weather.location.model.JsonModel.SearchLocations;
import com.mariebyleen.weather.location.model.WeatherLocation;
import com.mariebyleen.weather.navigation.Navigator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SearchLocationsTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    LocationViewContract view;
    @Mock
    WeatherLocation location;
    @Mock
    GeoNamesApiService apiService;
    @Mock
    WeatherDataService weatherDataService;


    private LocationViewModel viewModel;

    @Mock
    Resources resources;
    @Mock
    AutoCompleteTextView textView;

    private SharedPreferences preferences;
    private Navigator navigator;

    @Before
    public void init() {
        preferences = new FakeSharedPreferences();
        navigator = new Navigator();
        viewModel = new LocationViewModel(view, location, apiService, preferences, resources,
                navigator, weatherDataService);


    }

    @Test
    public void savesCoordinatesFromModel() {
        when(resources.getString(R.string.preference_latitude_key)).thenReturn("lat");
        when(resources.getString(R.string.preference_longitude_key)).thenReturn("lon");

        SearchLocations model = createFakeModel("Test", "Test", "Test", "12.345", "12.345");
        viewModel.setModel(model);

        when(view.getSearchTextViewText()).thenReturn("Test, Test, Test");

        viewModel.saveSearchLocationCoordinates();

        assertEquals(12.345f, preferences.getFloat("lat", 0.00f));
        assertEquals(12.345f, preferences.getFloat("lon", 0.00f));
    }

    private SearchLocations createFakeModel(String name, String stateName, String countryName,
                                            String lat, String lon) {
        SearchLocations model = new SearchLocations();
        SearchLocation[] geoNames = new SearchLocation[1];
        SearchLocation sampleLocation = new SearchLocation();
        sampleLocation.setToponymName(name);
        sampleLocation.setAdminName1(stateName);
        sampleLocation.setCountryName(countryName);
        sampleLocation.setLat(lat);
        sampleLocation.setLng(lon);
        geoNames[0] = sampleLocation;
        model.setGeonames(geoNames);
        return model;
    }
}
