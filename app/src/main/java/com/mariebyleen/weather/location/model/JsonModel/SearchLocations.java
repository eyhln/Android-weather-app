package com.mariebyleen.weather.location.model.JsonModel;

public class SearchLocations {
    private int totalResultsCount;
    private SearchLocation[] geonames;

    public int getTotalResultsCount() {
        return this.totalResultsCount;
    }

    public void setTotalResultsCount(int totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public SearchLocation[] getGeonames() {
        return this.geonames;
    }

    public void setGeonames(SearchLocation[] geonames) {
        this.geonames = geonames;
    }
}
