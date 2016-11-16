package com.mariebyleen.weather;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mariebyleen.weather.location.presenter.LocationPresenter;
import com.mariebyleen.weather.location.presenter.LocationViewContract;

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

    private LocationPresenter presenter;

    @Mock
    private LocationViewContract view;

    @Mock
    private Context context;

    @Mock
    private GoogleApiAvailability availability;

    @Mock
    private Log log;

    @Before
    public void initialize() {
        presenter = new LocationPresenter(context, availability, view);
    }

    @Test
    public void on_check_for_Google_Services_available_determine_true_when_receive_SUCCESS() {
        when(availability.isGooglePlayServicesAvailable(context))
                .thenReturn(ConnectionResult.SUCCESS);
        assertTrue(presenter.isPlayServicesAvailableOnDevice());
    }

    @Test
    public void on_check_for_Google_Services_available_determine_false_when_receive_SERVICE_MISSING() {
        when(availability.isGooglePlayServicesAvailable(context))
                .thenReturn(ConnectionResult.SERVICE_MISSING);
        assertFalse(presenter.isPlayServicesAvailableOnDevice());
    }
}
