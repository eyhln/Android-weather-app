package com.mariebyleen.weather.location.recent_locations.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private Context context;
    private TestDbHelper dbHelper;
    private DatabaseReadWriteImpl dbRW;
    private Database database;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new TestDbHelper(context);
        dbRW = new DatabaseReadWriteImpl(dbHelper);
        database = new Database(dbRW);
    }

    @Test
    public void testGetRecentLocations() {
        for (int i = 0; i < 2; i++) {
            float increment = i;
            dbRW.addRow("Test" + i, increment, increment, i);
        }

        RecentLocation[] recentLocations = database.getRecentLocations();

        assertEquals(2, recentLocations.length);

        RecentLocation testLocation1 = recentLocations[0];
        assertEquals("Test1", testLocation1.getName());
        assertEquals(1f, testLocation1.getLat());
        assertEquals(1f, testLocation1.getLon());

        RecentLocation testLocation0 = recentLocations[1];
        assertEquals("Test0", testLocation0.getName());
        assertEquals(0f, testLocation0.getLat());
        assertEquals(0f, testLocation0.getLon());
    }

    @Test
    public void testGetRecentLocations_noLocations() {
        RecentLocation[] recentLocations = database.getRecentLocations();

        assertEquals(0, recentLocations.length);
    }

    @Test
    public void testInsertRecentLocation() {
        RecentLocation location = new RecentLocation("Location", 0.0f, 0.0f);

        database.insertRecentLocation(location);

        int rowCount = getNumRows();

        Cursor cursor = getAllData();
        String name = cursor.getString(1);

        assertEquals(1, rowCount);
        assertEquals("Location", name);
    }

    @Test
    public void testInsertRecentLocation_listFull() {
        for (int i = 0; i < Database.MAX_ROWS; i++)
            dbRW.addRow("Test", 0.0f, 0.0f, 123456789);
        RecentLocation location = new RecentLocation("Location", 0.0f, 0.0f);

        database.insertRecentLocation(location);

        int numRows = getNumRows();
        Cursor cursor = getAllData();
        String name = cursor.getString(1);

        assertEquals(Database.MAX_ROWS, numRows);
        assertEquals("Location", name);

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
