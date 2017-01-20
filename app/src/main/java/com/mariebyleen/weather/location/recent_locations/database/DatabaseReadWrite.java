package com.mariebyleen.weather.location.recent_locations.database;

import android.database.Cursor;

public interface DatabaseReadWrite {

    Cursor getAllEntriesNewestFirst();
    Cursor getRowCount();
    void addRow(String name, float lat, float lon, long time);
}
