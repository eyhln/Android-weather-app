package com.mariebyleen.weather.location.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.location.view.LocationFragment;

public class LocationActivity extends BaseActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LocationActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.settings_display, false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        setContentView(R.layout.activity_fragment);

        if (fragment == null) {
            fragment = new LocationFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
