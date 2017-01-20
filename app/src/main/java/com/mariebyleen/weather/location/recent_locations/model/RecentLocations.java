package com.mariebyleen.weather.location.recent_locations.model;


import java.util.List;

public class RecentLocations {

    private List<RecentLocation> recentLocations;

    public RecentLocations(List<RecentLocation> recentLocations) {
        this.recentLocations = recentLocations;
    }

    public List<RecentLocation> getRecentLocations() {
        return recentLocations;
    }
}
