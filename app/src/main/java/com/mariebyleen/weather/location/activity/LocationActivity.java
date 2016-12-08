package com.mariebyleen.weather.location.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mariebyleen.weather.base.SingleFragmentActivity;
import com.mariebyleen.weather.location.view.LocationFragment;
import com.mariebyleen.weather.preferences.activity.PreferencesActivity;

public class LocationActivity extends SingleFragmentActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new LocationFragment();
    }

}
