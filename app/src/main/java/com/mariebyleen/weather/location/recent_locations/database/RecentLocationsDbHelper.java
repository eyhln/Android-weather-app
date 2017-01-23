package com.mariebyleen.weather.location.recent_locations.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.SQL_CREATE_ENTRIES;

public class RecentLocationsDbHelper extends SQLiteOpenHelper implements DbHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecentLocations.db";

    public RecentLocationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // TODO define behavior
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO define behavior
    }

}
