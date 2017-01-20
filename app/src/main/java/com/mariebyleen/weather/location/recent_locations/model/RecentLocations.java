package com.mariebyleen.weather.location.recent_locations.model;


public class RecentLocations {

    private RecentLocation[] recentLocations;

    public RecentLocations(RecentLocation[] recentLocations) {
        this.recentLocations = recentLocations;
    }

    public RecentLocation[] getRecentLocations() {
        return recentLocations;
    }
}
