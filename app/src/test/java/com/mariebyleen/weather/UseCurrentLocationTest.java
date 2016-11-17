package com.mariebyleen.weather;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mariebyleen.weather.location.model.CurrentLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UseCurrentLocationTest {

    private CurrentLocation finder;

    @Mock
    private Context context;

    @Mock
    private GoogleApiAvailability availability;

    @Mock
    private GoogleApiClient client;

    @Before
    public void initialize() {
        finder = new CurrentLocation(client, context, availability);
    }

    @Test
    public void on_check_for_Google_Services_available_determine_true_when_receive_SUCCESS() {
        when(availability.isGooglePlayServicesAvailable(context))
                .thenReturn(ConnectionResult.SUCCESS);
        assertTrue(finder.isPlayServicesAvailableOnDevice());
    }

    @Test
    public void on_check_for_Google_Services_available_determine_false_when_receive_SERVICE_MISSING() {
        when(availability.isGooglePlayServicesAvailable(context))
                .thenReturn(ConnectionResult.SERVICE_MISSING);
        assertFalse(finder.isPlayServicesAvailableOnDevice());
    }
}
