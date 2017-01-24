package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;

import java.util.List;

import javax.inject.Inject;

import static com.mariebyleen.weather.location.presenter.LocationPresenter.MAX_RECENT_LOCATIONS;

public class RecentLocationsUpdater {

    private final int NO_MATCH_FOUND = -1000;

    private List<RecentLocation> recentLocations;

    @Inject
    public RecentLocationsUpdater() {}

    public RecentLocations updateRecentLocations(RecentLocations model,
                                                            RecentLocation location) {
        if (location != null && location.getName() != null) {
            this.recentLocations = model.getRecentLocations();
            deleteAnyDuplicateEntry(location);
            if (recentLocations.size() >= MAX_RECENT_LOCATIONS)
                deleteOldestEntry();
            recentLocations.add(0, location);
            RecentLocations newModel = new RecentLocations(recentLocations);
            return newModel;
        }
        return model;
    }

    private void deleteAnyDuplicateEntry(RecentLocation location) {
        int matchIndex = getIndexOfAnyMatchingEntry(location);
        if (matchIndex != NO_MATCH_FOUND) {
            RecentLocation matchLocation = recentLocations.get(matchIndex);
            recentLocations.remove(matchLocation);
        }
    }

    private int getIndexOfAnyMatchingEntry(RecentLocation location) {
        for (int i = 0; i < recentLocations.size(); i++) {
            if (location.equals(recentLocations.get(i))) {
                return i;
            }
        }
        return NO_MATCH_FOUND;
    }

    private void deleteOldestEntry() {
        recentLocations.remove(recentLocations.size() - 1);
    }
}
