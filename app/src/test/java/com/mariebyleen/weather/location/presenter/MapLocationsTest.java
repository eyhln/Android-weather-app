package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.search_locations.model.SearchLocation;
import com.mariebyleen.weather.location.search_locations.model.SearchLocations;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MapLocationsTest {

    private SelectLocationMapper mapper;

    @Before
    public void init() {
        mapper = new SelectLocationMapper();
    }

    @Test
    public void testMapSearchLocationNames() {
        SearchLocation[] locations = new SearchLocation[2];
        locations[0] = getSearchLocation("0");
        locations[1] = getSearchLocation("1");
        SearchLocations searchLocations = new SearchLocations();
        searchLocations.setLocations(locations);

        String[] names = mapper.mapSearchLocationNames(searchLocations);

        assertEquals("TopName0, AdminName0, CountryName0", names[0]);
        assertEquals("TopName1, AdminName1, CountryName1", names[1]);

    }

    private SearchLocation getSearchLocation(String identifier) {
        SearchLocation location = new SearchLocation();
        location.setToponymName("TopName" + identifier);
        location.setAdminName1("AdminName" + identifier);
        location.setCountryName("CountryName" + identifier);
        location.setLat("100.0");
        location.setLng("-100.0");
        return location;
    }

    @Test
    public void testMapSearchLocationToRecentLocation() {
        SearchLocation location = getSearchLocation("");

        RecentLocation recentLocation = mapper.mapSearchLocationToRecentLocation(location);

        assertEquals("TopName, AdminName, CountryName", recentLocation.getName());
        assertEquals(100.0f, recentLocation.getLat());
        assertEquals(-100.0f, recentLocation.getLon());
        assertTrue(recentLocation.getTime() < System.currentTimeMillis());
    }
}
