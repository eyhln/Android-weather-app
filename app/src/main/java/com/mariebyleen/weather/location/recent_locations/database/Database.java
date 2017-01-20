package com.mariebyleen.weather.location.recent_locations.database;

import android.database.Cursor;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;

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
        RecentLocation[] locations = recentLocations.getRecentLocations();
        for (int i = 0; i < locations.length; i++) {
            RecentLocation location = locations[i];
            db.addRow(location.getName(), location.getLat(), location.getLon(), location.getTime());
        }
    }

    public RecentLocations getRecentLocations() {
        Cursor cursor = db.getAllEntriesNewestFirst();
        int numRows = getRowCount();
        RecentLocation[] locations = readCursorDataToRecentLocationArray(cursor, numRows);
        return new RecentLocations(locations);
    }

    private RecentLocation[] readCursorDataToRecentLocationArray(Cursor cursor, int numRows) {
        cursor.moveToFirst();
        RecentLocation[] recentLocations = new RecentLocation[numRows];
        for (int i = 0; i < numRows; i++) {
            String name = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME)));
            float lat = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LATITUDE)));
            float lon = (cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LONGITUDE)));
            int time = (cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ADD_TIME)));
            RecentLocation location = new RecentLocation(name, lat, lon, time);
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
