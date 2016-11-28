package com.mariebyleen.weather.settings.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mariebyleen.weather.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_display);
    }
}
