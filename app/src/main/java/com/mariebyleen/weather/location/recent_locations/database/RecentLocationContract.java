package com.mariebyleen.weather.location.recent_locations.database;

import android.provider.BaseColumns;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_ADD_TIME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LATITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LONGITUDE;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;

public final class RecentLocationContract {

    private RecentLocationContract() {}

    public static class RecentLocationRow implements BaseColumns {
        public static final String TABLE_NAME = "recent_location";
        public static final String COLUMN_NAME_LOCATION_NAME = "location_name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ADD_TIME = "time";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecentLocationRow.TABLE_NAME + " (" +
                    RecentLocationRow._ID + " INTEGER PRIMARY KEY," +
                    RecentLocationRow.COLUMN_NAME_LOCATION_NAME + " TEXT," +
                    RecentLocationRow.COLUMN_NAME_LATITUDE + " REAL," +
                    RecentLocationRow.COLUMN_NAME_LONGITUDE + " REAL," +
                    RecentLocationRow.COLUMN_NAME_ADD_TIME + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RecentLocationRow.TABLE_NAME;

    public static final String SQL_ADD_ROW =
            "INSERT INTO " + TABLE_NAME +
                    " (" + COLUMN_NAME_LOCATION_NAME + ", " +
                    COLUMN_NAME_LATITUDE + ", " +
                    COLUMN_NAME_LONGITUDE + ", " +
                    COLUMN_NAME_ADD_TIME + ") " +
            "VALUES (%d, , ?, ?)";

    public static final String SQL_DELETE_OLDEST_ENTRY =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE(" + COLUMN_NAME_ADD_TIME + ")" +
                    " IN (SELECT MIN(" + COLUMN_NAME_ADD_TIME + ")" +
                    " FROM " + TABLE_NAME + ");";

    public static final String SQL_SELECT_ALL_ENTRIES_NEWEST_FIRST =
            "SELECT * FROM " + TABLE_NAME +
                    " ORDER BY " + COLUMN_NAME_ADD_TIME + " DESC";

    public static final String SQL_NUMBER_OF_ROWS =
            "SELECT COUNT(*) FROM " + TABLE_NAME;

    public static final String SQL_DELETE_NAME_MATCH_ENTRY =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME_LOCATION_NAME + "=?;";
}
