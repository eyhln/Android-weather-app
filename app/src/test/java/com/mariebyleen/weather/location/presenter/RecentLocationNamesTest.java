package com.mariebyleen.weather.location.presenter;

import com.mariebyleen.weather.location.recent_locations.database.Database;
import com.mariebyleen.weather.location.recent_locations.model.RecentLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class RecentLocationNamesTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Database database;

    private LocationPresenter presenter;

    @Before
    public void init() {
        presenter = new LocationPresenter(null, null, null, null, null, database);
    }

    @Test
    public void RecentLocation_array_parses_to_String_array() {
        RecentLocation[] testLocations = getFakeRecentLocations("Test1", "Test2");
        when(database.getRecentLocations()).thenReturn(testLocations);

        String[] names = presenter.getRecentLocationNames();

        String[] expected = {"Test1", "Test2"};
        assertEquals(expected[0], names[0]);
        assertEquals(expected[1], names[1]);
    }

    private RecentLocation[] getFakeRecentLocations(String name1, String name2) {
        RecentLocation[] testLocations = new RecentLocation[2];
        RecentLocation testLocation0 = new RecentLocation(name1, 0.0f, 0.0f);
        testLocations[0] = testLocation0;
        RecentLocation testLocation1 = new RecentLocation(name2, 0.0f, 0.0f);
        testLocations[1] = testLocation1;
        return testLocations;
    }

    @Test
    public void null_entries_are_removed() {
        RecentLocation[] recentLocations = getFakeRecentLocationsWithNullEntry("Test");
        when(database.getRecentLocations()).thenReturn(recentLocations);

        String[] names = presenter.getRecentLocationNames();

        String[] expected = {"Test"};
        assertEquals(expected.length, names.length);
        assertEquals(expected[0], names[0]);
    }

    private RecentLocation[] getFakeRecentLocationsWithNullEntry(String name) {
        RecentLocation[] testLocations = new RecentLocation[2];
        RecentLocation testLocation0 = new RecentLocation(name, 0.0f, 0.0f);
        testLocations[0] = testLocation0;
        RecentLocation testLocation1 = null;
        testLocations[1] = testLocation1;
        return testLocations;
    }

    @Test
    public void if_no_data_returns_empty_array() {
        RecentLocation[] recentLocations = null;
        when(database.getRecentLocations()).thenReturn(recentLocations);

        String[] names = presenter.getRecentLocationNames();

        assertNotNull(names);
        assertEquals(0, names.length);
    }
}
