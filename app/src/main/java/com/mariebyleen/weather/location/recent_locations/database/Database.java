package com.mariebyleen.weather.location.recent_locations.database;

import android.database.Cursor;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;

import java.util.ArrayList;
import java.util.List;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_ADD_TIME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LATITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LONGITUDE;

public class Database {

    private DatabaseReadWrite db;

    public Database(DatabaseReadWrite db) {
        this.db = db;
    }

    public void insertRecentLocations(RecentLocations recentLocations) {
        db.clearAllEntries();
        List<RecentLocation> locations = recentLocations.getRecentLocations();
        for (int i = 0; i < locations.size(); i++) {
            RecentLocation location = locations.get(i);
            db.addRow(location.getName(), location.getLat(), location.getLon(), location.getTime());
        }
    }

    public RecentLocations getRecentLocations() {
        Cursor cursor = db.getAllEntriesNewestFirst();
        int numRows = getRowCount();
        List<RecentLocation> locations = readCursorDataToRecentLocationArray(cursor, numRows);
        return new RecentLocations(locations);
    }

    private List<RecentLocation> readCursorDataToRecentLocationArray(Cursor cursor, int numRows) {
        cursor.moveToFirst();
        List<RecentLocation> recentLocations = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            String name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME)));
            float lat = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LATITUDE)));
            float lon = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LONGITUDE)));
            int time = (cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ADD_TIME)));
            RecentLocation location = new RecentLocation(name, lat, lon, time);
            recentLocations.add(location);
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
