package com.mariebyleen.weather.location.recent_locations.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.COLUMN_NAME_LOCATION_NAME;
import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private Context context;
    private TestDbHelper dbHelper;
    private DatabaseReadWrite dbRW;
    private Database database;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new TestDbHelper(context);
        dbRW = new DatabaseReadWrite(dbHelper);
        database = new Database(dbRW);
    }

    @Test
    public void testGetRecentLocations() {
        for (int i = 0; i < 2; i++) {
            float increment = i;
            dbRW.addRow("Test" + i, increment, increment, i);
        }

        RecentLocations recentLocations = database.getRecentLocations();

        assertEquals(2, recentLocations.getRecentLocations().size());

        RecentLocation testLocation1 = recentLocations.getRecentLocations().get(0);
        assertEquals("Test1", testLocation1.getName());
        assertEquals(1f, testLocation1.getLat());
        assertEquals(1f, testLocation1.getLon());

        RecentLocation testLocation0 = recentLocations.getRecentLocations().get(1);
        assertEquals("Test0", testLocation0.getName());
        assertEquals(0f, testLocation0.getLat());
        assertEquals(0f, testLocation0.getLon());
    }

    @Test
    public void testGetRecentLocations_noLocations() {
        RecentLocations recentLocations = database.getRecentLocations();

        assertEquals(0, recentLocations.getRecentLocations().size());
    }

    @Test
    public void testInsertRecentLocations() {
        List<RecentLocation> locations = new ArrayList<>();
        locations.add(new RecentLocation("Test1", 0.0f, 0.0f, 12345));
        locations.add(new RecentLocation("Test2", 0.0f, 0.0f, 12346));
        RecentLocations recentLocations = new RecentLocations(locations);

        database.insertRecentLocations(recentLocations);

        int numRows = getNumRows();
        Cursor cursor = getAllData();
        String name1 = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME));
        cursor.moveToNext();
        String name2 = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LOCATION_NAME));

        assertEquals(2, numRows);
        assertEquals("Test1", name1);
        assertEquals("Test2", name2);
    }

    private int getNumRows() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor count = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        count.moveToFirst();
        return count.getInt(0);
    }

    private Cursor getAllData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        data.moveToFirst();
        return data;
    }

    @After
    public void emptyDb() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
