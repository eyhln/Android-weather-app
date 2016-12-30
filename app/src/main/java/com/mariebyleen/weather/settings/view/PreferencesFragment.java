package com.mariebyleen.weather.settings.view;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.android.annotations.Nullable;
import com.mariebyleen.weather.R;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.ButterKnife;

public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

    private Preference temperatureUnitsOfMeasurement;
    @BindArray(R.array.units_of_measurement)
    String[] temperatureMeasurementDisplayValues;
    @BindArray(R.array.units_of_measurement_code)
    String[] temperatureMeasurementStoredValues;

    private Preference speedUnitsOfMeasurement;
    @BindArray(R.array.speed_units_of_measurement)
    String[] speedMeasurementDisplayValues;
    @BindArray(R.array.speed_units_of_measurement_code)
    String[] speedMeasurementStoredValues;

    private Preference dataRefreshPeriod;
    @BindArray(R.array.time_unit_display)
    String[] dataRefreshPeriodDisplayValues;
    @BindArray(R.array.time_unit_in_milliseconds)
    String[] dataRefreshPeriodStoredValues;
    @BindString(R.string.update_period_summary_specific)
    String updatePeriodSummary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this, getActivity());
        addPreferencesFromResource(R.xml.settings_display);
        updatePreferenceSummariesWithCurrentValues();
    }

    private void updatePreferenceSummariesWithCurrentValues() {
        updateTemperatureMeasurementPreferenceSummary();
        updateSpeedMeasurementPreferenceSummary();
        updateDataRefreshPeriodPreferenceSummary();
    }

        private void updateTemperatureMeasurementPreferenceSummary() {
            temperatureUnitsOfMeasurement = findPreference("UNITS");
            String unitsCode = getStoredValue(temperatureUnitsOfMeasurement);
            updateSummary(temperatureUnitsOfMeasurement, getTemperatureMeasurementDisplayValue(unitsCode));
            temperatureUnitsOfMeasurement.setOnPreferenceChangeListener(this);
        }

        private void updateSpeedMeasurementPreferenceSummary() {
            speedUnitsOfMeasurement = findPreference("SPEED_UNITS");
            String unitsCode = getStoredValue(speedUnitsOfMeasurement);
            updateSummary(speedUnitsOfMeasurement, getSpeedMeasurementDisplayValue(unitsCode));
            speedUnitsOfMeasurement.setOnPreferenceChangeListener(this);
        }

        private void updateDataRefreshPeriodPreferenceSummary() {
            dataRefreshPeriod = findPreference("UPDATE_PERIOD");
            String updateTimeMs = getStoredValue(dataRefreshPeriod);
            updateSummary(dataRefreshPeriod, getDataRefreshPeriodDisplayValue(updateTimeMs));
            dataRefreshPeriod.setOnPreferenceChangeListener(this);
        }

        private String getStoredValue(Preference preference) {
            // TODO set default value of units based on primary Locale
            String key = preference.getKey();
            return preference.getSharedPreferences().getString(key, null);
        }

        private String getTemperatureMeasurementDisplayValue(String storedValue) {
            return getDisplayValue(storedValue, temperatureMeasurementStoredValues,
                    temperatureMeasurementDisplayValues);
        }

        private String getSpeedMeasurementDisplayValue(String storedValue) {
            switch (storedValue) {
                case "ms":
                    return getString(R.string.mps_long);
                case "kph":
                    return getString(R.string.kph_long);
                case "mph":
                    return getString(R.string.mph_long);
                default:
                    return null;
            }
        }

        private String getDataRefreshPeriodDisplayValue(String storedValue) {
            String displayValue = getDisplayValue(storedValue, dataRefreshPeriodStoredValues,
                    dataRefreshPeriodDisplayValues);
            return String.format(updatePeriodSummary, displayValue);
        }

        @Nullable
        private String getDisplayValue(String storedValue, String[] storedValues,
                                       String[] displayValues) {
            for (int i = 0; i < storedValues.length; i++) {
                if (storedValue.equals(storedValues[i]))
                    return displayValues[i];
            }
            return null;
        }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.equals(dataRefreshPeriod))
            updateSummary(preference, getDataRefreshPeriodDisplayValue(o.toString()));
        if (preference.equals(temperatureUnitsOfMeasurement))
            updateSummary(preference, getTemperatureMeasurementDisplayValue(o.toString()));
        if (preference.equals(speedUnitsOfMeasurement))
            updateSummary(preference, getSpeedMeasurementDisplayValue(o.toString()));
        return true;
    }

    private void updateSummary(Preference preference, String value) {
        if (value == null || value.length() == 0) {
            if (preference.equals(dataRefreshPeriod))
                preference.setSummary(R.string.update_period_summary_default);
            if (preference.equals(temperatureUnitsOfMeasurement))
                preference.setSummary(R.string.units_default_summary);
            if (preference.equals(speedUnitsOfMeasurement))
                preference.setSummary(R.string.speed_units_default_summary);
        }
        else
            preference.setSummary(value);
    }
}
