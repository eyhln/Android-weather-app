package com.mariebyleen.weather.navigation;

import android.content.Context;
import android.content.Intent;

import com.mariebyleen.weather.location.activity.LocationActivity;
import com.mariebyleen.weather.preferences.activity.PreferencesActivity;

import javax.inject.Inject;

public class Navigator {

    @Inject
    public Navigator() {}

    public void navigateToPreferences(Context context) {
        if (context != null) {
            Intent intentToLaunch = PreferencesActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToLocationEditor(Context context) {
        if (context != null) {
            Intent intentToLaunch = LocationActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
