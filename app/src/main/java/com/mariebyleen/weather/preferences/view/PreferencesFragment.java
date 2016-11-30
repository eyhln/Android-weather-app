package com.mariebyleen.weather.preferences.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mariebyleen.weather.R;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_display);
    }

}
