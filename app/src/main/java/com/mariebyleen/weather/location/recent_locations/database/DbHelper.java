package com.mariebyleen.weather.location.recent_locations.database;

import android.database.sqlite.SQLiteDatabase;

public interface DbHelper {

    SQLiteDatabase getReadableDatabase();
    SQLiteDatabase getWritableDatabase();
}
