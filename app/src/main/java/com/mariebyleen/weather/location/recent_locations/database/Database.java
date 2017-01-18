package com.mariebyleen.weather.location.recent_locations.database;

import android.database.Cursor;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LATITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LONGITUDE;

public class Database {

    private final int MAX_ROWS = 10;
    private final int NO_MATCH_FOUND = -1000;

    private DatabaseReadWrite db;

    public Database(DatabaseReadWrite db) {
        this.db = db;
    }

    public void insertRecentLocation(String name, float lat, float lon) {

        RecentLocation[] recentLocations = getRecentLocations();
        if (recentLocations.length >= MAX_ROWS) {
            db.deleteOldestEntry();
        }
        int positionOfMatch = matchingEntryAlreadyListed(name, recentLocations);
        if (positionOfMatch != NO_MATCH_FOUND) {
            String duplicatedName = recentLocations[positionOfMatch].getName();
            db.deleteNameMatchEntry(duplicatedName);
        }
        long time = System.currentTimeMillis();
        db.addRow(name, lat, lon, time);
    }

    private int matchingEntryAlreadyListed(String name, RecentLocation[] recentLocations) {
        if (recentLocations != null) {
            for (int i = 0; i < recentLocations.length; i++) {
                if (name.equals(recentLocations[i].getName()))
                    return i;
            }
        }
        return NO_MATCH_FOUND;
    }

    public RecentLocation[] getRecentLocations() {
        Cursor cursor = db.getAllEntriesNewestFirst();
        int numRows = getRowCount();
        return readCursorDataToRecentLocationArray(cursor, numRows);
    }

    private RecentLocation[] readCursorDataToRecentLocationArray(Cursor cursor, int numRows) {
        cursor.moveToFirst();
        RecentLocation[] recentLocations = new RecentLocation[numRows];
        for (int i = 0; i < numRows; i++) {
            String name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME)));
            float lat = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LATITUDE)));
            float lon = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LONGITUDE)));
            RecentLocation location = new RecentLocation(name, lat, lon);
            recentLocations[i] = location;
            cursor.moveToNext();
        }
        return recentLocations;
    }

    private int getRowCount() {
        Cursor numEntries = db.getRowCount();
        numEntries.moveToFirst();
        return numEntries.getInt(0);
    }
}
