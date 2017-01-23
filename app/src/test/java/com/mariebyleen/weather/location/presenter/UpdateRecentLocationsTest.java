package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mariebyleen.weather.location.presenter.LocationPresenter.MAX_RECENT_LOCATIONS;
import static junit.framework.Assert.assertEquals;

public class UpdateRecentLocationsTest {

    private RecentLocationsUpdater utils;

    private List<RecentLocation> locations;

    @Before
    public void initialize() {
        utils = new RecentLocationsUpdater();
        locations = new ArrayList<>();
    }

    @Test
    public void simpleAddEntry() {
        RecentLocations recentLocations = new RecentLocations(locations);
        RecentLocation location = new RecentLocation("Test", 0.0f, 0.0f, 123456789);

        RecentLocations updatedLocations =
                utils.updateRecentLocations(recentLocations, location);

        List<RecentLocation> updatedList = updatedLocations.getRecentLocations();
        RecentLocation retrievedLocation = updatedList.get(0);
        assertEquals(location.getName(), retrievedLocation.getName());
    }

    @Test
    public void listAlreadyAtMaxLength_deletesOldestEntry() {
        for (int i = 0; i < MAX_RECENT_LOCATIONS; i++) {
            locations.add(new RecentLocation("Test", 0.0f, 0.0f, MAX_RECENT_LOCATIONS - i));
        }
        RecentLocations recentLocations = new RecentLocations(locations);
        RecentLocation addLocation = new RecentLocation("AddMe", 0.0f, 0.0f, 100);

        RecentLocations returnLocations =
                utils.updateRecentLocations(recentLocations, addLocation);

        List<RecentLocation> locationsList = returnLocations.getRecentLocations();
        assertEquals(addLocation, locationsList.get(0));
        assertEquals(new RecentLocation("Test", 0.0f, 0.0f, MAX_RECENT_LOCATIONS),
                locationsList.get(MAX_RECENT_LOCATIONS - 1));
    }

    @Test
    public void selectFromList_removesDuplicateEntry() {
        locations.add(new RecentLocation("Test", 0.0f, 0.0f, 1));
        RecentLocations recentLocations = new RecentLocations(locations);
        RecentLocation duplicateLocation = new RecentLocation("Test", 0.0f, 0.0f, 2);

        RecentLocations returnLocations =
                utils.updateRecentLocations(recentLocations, duplicateLocation);

        List<RecentLocation> returnList = returnLocations.getRecentLocations();
        assertEquals(1, returnList.size());
        assertEquals(duplicateLocation, returnList.get(0));
    }


}
