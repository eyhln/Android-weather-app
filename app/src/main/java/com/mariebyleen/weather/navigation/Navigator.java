package com.mariebyleen.weather.navigation;

import android.content.Context;
import android.content.Intent;

import com.mariebyleen.weather.location.activity.LocationActivity;
import com.mariebyleen.weather.settings.activity.SettingsActivity;
import com.mariebyleen.weather.weather_display.activity.MainActivity;

import javax.inject.Inject;

public class Navigator {

    @Inject
    public Navigator() {}

    public void navigateToPreferences(Context context) {
        if (context != null) {
            Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToLocationEditor(Context context) {
        if (context != null) {
            Intent intentToLaunch = LocationActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToMain(Context context) {
        if (context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
