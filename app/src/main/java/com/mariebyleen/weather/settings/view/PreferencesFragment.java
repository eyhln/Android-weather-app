package com.mariebyleen.weather.settings.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.mariebyleen.weather.R;

import butterknife.BindArray;
import butterknife.ButterKnife;

public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

    SharedPreferences sharedPreferences;
    @BindArray(R.array.units_of_measurement)
    String[] measurementSystem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this, getActivity());
        addPreferencesFromResource(R.xml.settings_display);
        updatePreferenceSummariesWithCurrentValues();
    }

    private void updatePreferenceSummariesWithCurrentValues() {
        Preference unitsOfMeasurement = findPreference("UNITS");
        String key = unitsOfMeasurement.getKey();
        SharedPreferences preferences = unitsOfMeasurement.getSharedPreferences();
        String unitsCode = preferences.getString(key, null);
        String value = getUnitsFromCode(unitsCode);
        updateSummary(unitsOfMeasurement, value);
        unitsOfMeasurement.setOnPreferenceChangeListener(this);
    }

    private String getUnitsFromCode(String unitsCode) {
        return measurementSystem[Integer.parseInt(unitsCode)];
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String value = getUnitsFromCode(o.toString());
        updateSummary(preference, value);
        return true;
    }

    private void updateSummary(Preference preference, String value) {
        if (value == null || value.length() == 0)
            preference.setSummary(R.string.units_default_summary);
        else
            preference.setSummary(value);
    }
}
