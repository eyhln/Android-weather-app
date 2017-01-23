package com.mariebyleen.weather.location.search_locations.model;

public class SearchLocations {
    private int totalResultsCount;
    private SearchLocation[] locations;

    public int getTotalResultsCount() {
        return this.totalResultsCount;
    }

    public void setTotalResultsCount(int totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public SearchLocation[] getLocations() {
        return this.locations;
    }

    public void setLocations(SearchLocation[] locations) {
        this.locations = locations;
    }
}
