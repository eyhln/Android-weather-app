package com.mariebyleen.weather.location.recent_locations.database;

import android.database.Cursor;

public interface DatabaseReadWrite {

    Cursor getAllEntriesNewestFirst();
    Cursor getRowCount();
    void deleteOldestEntry();
    void deleteNameMatchEntry(String duplicatedName);
    void addRow(String name, float lat, float lon, long time);
}
