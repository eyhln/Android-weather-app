package com.mariebyleen.weather.location.recent_locations.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mariebyleen.weather.location.recent_locations.database.RecentLocationContract.RecentLocationRow.TABLE_NAME;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseReadWriteTest {

    private Context context;
    private TestDbHelper dbHelper;
    private DatabaseReadWriteImpl dbRW;

    @Before
    public void initialize() {
        context = InstrumentationRegistry.getTargetContext();
        dbHelper = new TestDbHelper(context);
        dbRW = new DatabaseReadWriteImpl(dbHelper);
    }

    @Test
    public void testAddRow() {
        dbRW.addRow("Test", 0.0f, 0.0f, 123456789);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        float lat = cursor.getFloat(2);
        float lon = cursor.getFloat(3);
        int time = cursor.getInt(4);
        cursor.close();

        assertEquals(1, id);
        assertEquals("Test", name);
        assertEquals(0.0f, lat);
        assertEquals(0.0f, lon);
        assertEquals(123456789, time);
    }

    @Test
    public void testGetAllEntriesNewestFirst() {
        for (int i = 0; i < 3; i++)
            dbRW.addRow("Test", 0.0f, 0.0f, i);

        Cursor cursor = dbRW.getAllEntriesNewestFirst();

        cursor.moveToFirst();
        int newest = cursor.getInt(4);
        cursor.moveToNext();
        int middle = cursor.getInt(4);
        cursor.moveToNext();
        int last = cursor.getInt(4);

        assertTrue(newest > middle && middle > last);
    }

    @Test
    public void testGetRowCount() {
        for (int i = 0; i < 3; i++)
            dbRW.addRow("Test", 0.0f, 0.0f, i);

        Cursor cursor = dbRW.getRowCount();

        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);

        assertEquals(3, rowCount);
    }

    @After
    public void emptyDb() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
