package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.search_locations.model.SearchLocation;
import com.mariebyleen.weather.location.search_locations.model.SearchLocations;

import javax.inject.Inject;

public class SelectLocationMapper {

    @Inject
    public SelectLocationMapper() {}


    public String[] mapSearchLocationNames(SearchLocations searchLocations) {
        SearchLocation[] locations = searchLocations.getLocations();
        if (locations != null && locations.length > 0) {
            String[] names = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                names[i] = mapLocationName(locations[i]);
            }
            return names;
        }
        return new String[0];
    }

    public String mapLocationName(SearchLocation location) {
        String name = location.getToponymName();
        String admin = location.getAdminName1();
        String country = location.getCountryName();
        return String.format("%s, %s, %s", name, admin, country);
    }

    public RecentLocation mapSearchLocationToRecentLocation(SearchLocation location) {
        String name = mapLocationName(location);
        float lat = Float.parseFloat(location.getLat());
        float lon = Float.parseFloat(location.getLng());
        long time = System.currentTimeMillis();
        return new RecentLocation(name, lat, lon, time);
    }
}
