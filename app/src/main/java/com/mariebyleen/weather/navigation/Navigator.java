package com.mariebyleen.weather.navigation;

import android.content.Context;
import android.content.Intent;

import com.mariebyleen.weather.settings.activity.SettingsActivity;

import javax.inject.Inject;

public class Navigator {

    @Inject
    public Navigator() {}

    public void navigateToSettings(Context context) {
        if (context != null) {
            Intent intentToLaunch = SettingsActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
