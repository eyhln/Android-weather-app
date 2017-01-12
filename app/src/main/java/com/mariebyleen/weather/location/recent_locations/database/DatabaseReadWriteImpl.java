package com.mariebyleen.weather.location.recent_locations.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_ADD_TIME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LATITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LONGITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_DELETE_NAME_MATCH_ENTRY;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_DELETE_OLDEST_ENTRY;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_NUMBER_OF_ROWS;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_SELECT_ALL_ENTRIES_NEWEST_FIRST;

public class DatabaseReadWriteImpl implements DatabaseReadWrite {

    private RecentLocationsDbHelper dbHelper;

    public DatabaseReadWriteImpl(RecentLocationsDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Cursor getAllEntriesNewestFirst() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(SQL_SELECT_ALL_ENTRIES_NEWEST_FIRST, null);
    }

    public Cursor getRowCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(SQL_NUMBER_OF_ROWS, null);
    }

    public void deleteOldestEntry() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_OLDEST_ENTRY);
    }

    public void deleteNameMatchEntry(String duplicatedName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Object[] matchingName = new Object[1];
        matchingName[0] = duplicatedName;
        db.execSQL(SQL_DELETE_NAME_MATCH_ENTRY, matchingName);
    }

    public void addRow(String name, float lat, float lon, long time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LOCATION_NAME, name);
        values.put(COLUMN_NAME_LATITUDE, lat);
        values.put(COLUMN_NAME_LONGITUDE, lon);
        values.put(COLUMN_NAME_ADD_TIME, time);
        db.insert(TABLE_NAME, null, values);
    }
}
