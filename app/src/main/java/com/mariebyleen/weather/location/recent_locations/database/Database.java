package com.mariebyleen.weather.location.recent_locations.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_ADD_TIME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LATITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LONGITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_DELETE_NAME_MATCH_ENTRY;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_DELETE_OLDEST_ENTRY;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_NUMBER_OF_ROWS;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_SELECT_ALL_ENTRIES_NEWEST_FIRST;

public class Database {

    private final int MAX_ROWS = 10;
    private final int NO_MATCH_FOUND = -1000;

    private RecentLocationsDbHelper dbHelper;

    public Database(RecentLocationsDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insertRecentLocation(String name, float lat, float lon) {
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        RecentLocation[] recentLocations = getRecentLocations();
        if (recentLocations.length >= MAX_ROWS) {
            dbWrite.execSQL(SQL_DELETE_OLDEST_ENTRY);
        }
        int positionOfMatch = matchingEntryAlreadyListed(name, recentLocations);
        if (positionOfMatch != NO_MATCH_FOUND) {
            Object[] matchingName = new Object[1];
            matchingName[0] = recentLocations[positionOfMatch].getName();
            dbWrite.execSQL(SQL_DELETE_NAME_MATCH_ENTRY, matchingName);
        }
        addRow(name, lat, lon);
    }

        private int matchingEntryAlreadyListed(String name, RecentLocation[] recentLocations) {
            for (int i = 0; i < recentLocations.length; i++) {
                if (name.equals(recentLocations[i].getName()))
                    return i;
            }
            return NO_MATCH_FOUND;
        }

        private void addRow(String name, float lat, float lon) {
            long time = System.currentTimeMillis();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_LOCATION_NAME, name);
            values.put(COLUMN_NAME_LATITUDE, lat);
            values.put(COLUMN_NAME_LONGITUDE, lon);
            values.put(COLUMN_NAME_ADD_TIME, time);
            db.insert(TABLE_NAME, null, values);
        }

    public RecentLocation[] getRecentLocations() {
        Cursor cursor = getAllEntriesNewestFirst();
        int numRows = getRowCount();
        return readCursorDataToRecentLocationArray(cursor, numRows);
    }

    private Cursor getAllEntriesNewestFirst() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(SQL_SELECT_ALL_ENTRIES_NEWEST_FIRST, null);
    }

    private RecentLocation[] readCursorDataToRecentLocationArray(Cursor cursor, int numRows) {
        cursor.moveToFirst();
        RecentLocation[] recentLocations = new RecentLocation[numRows];
        for (int i = 0; i < numRows; i++) {
            RecentLocation location = new RecentLocation();
            location.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME)));
            location.setLat(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LATITUDE)));
            location.setLon(cursor.getFloat(cursor.getColumnIndex(COLUMN_NAME_LONGITUDE)));
            recentLocations[i] = location;
            cursor.moveToNext();
        }
        return recentLocations;
    }

    private String[] readCursorDataNamesToStringArray(Cursor cursor, int numRows) {
        cursor.moveToFirst();
        String[] names = new String[numRows];
        for (int i = 0; i < names.length; i++) {
            names[i] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME));
            cursor.moveToNext();
        }
        return names;
    }

    private int getRowCount() {
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        Cursor numEntries = dbRead.rawQuery(SQL_NUMBER_OF_ROWS, null);
        numEntries.moveToFirst();
        return numEntries.getInt(0);
    }
}
