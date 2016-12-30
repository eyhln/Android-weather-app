package com.mariebyleen.weather.settings.view;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.android.annotations.Nullable;
import com.mariebyleen.weather.R;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.ButterKnife;

public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private final String LAST_SPEED_WITH_CELSIUS_KEY = "lastSpeed";

    private ListPreference temperatureUnitsOfMeasurement;
    @BindArray(R.array.units_of_measurement)
    String[] temperatureMeasurementDisplayValues;
    @BindArray(R.array.units_of_measurement_code)
    String[] temperatureMeasurementStoredValues;

    private ListPreference speedUnitsOfMeasurement;
    @BindArray(R.array.speed_units_of_measurement_summary_display)
    String[] speedMeasurementDisplayValues;
    @BindArray(R.array.speed_units_of_measurement_code)
    String[] speedMeasurementStoredValues;

    private ListPreference dataRefreshPeriod;
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
        referencePreferences();
        setSpeedUnitsPreferenceBasedOnTempUnits(getStoredValue(temperatureUnitsOfMeasurement));
        updatePreferenceSummariesWithCurrentValues();
    }

    private void referencePreferences() {
        dataRefreshPeriod = (ListPreference)findPreference("UPDATE_PERIOD");
        temperatureUnitsOfMeasurement = (ListPreference)findPreference("UNITS");
        speedUnitsOfMeasurement = (ListPreference)findPreference("SPEED_UNITS");
    }

    private void setSpeedUnitsPreferenceBasedOnTempUnits(String storedValue) {
        if (storedValue.equals("Fahrenheit")){
            speedUnitsOfMeasurement.setValue("mph");
            speedUnitsOfMeasurement.setEnabled(false);
        }
        if (storedValue.equals("Celsius")) {
            setSpeedPrefToLastUsedSpeedWithCelsius();
            speedUnitsOfMeasurement.setEnabled(true);
        }
        updatePreferenceSummary(speedUnitsOfMeasurement);
    }

        private void saveLastUsedSpeedWithCelsius(String storedValue) {
            speedUnitsOfMeasurement.getSharedPreferences().edit()
                    .putString(LAST_SPEED_WITH_CELSIUS_KEY, storedValue).apply();
        }

        private void setSpeedPrefToLastUsedSpeedWithCelsius() {
            String savedValue = speedUnitsOfMeasurement.getSharedPreferences()
                    .getString(LAST_SPEED_WITH_CELSIUS_KEY, null);
            speedUnitsOfMeasurement.setValue(savedValue);
        }

    private void updatePreferenceSummariesWithCurrentValues() {
        updatePreferenceSummary(dataRefreshPeriod);
        updatePreferenceSummary(temperatureUnitsOfMeasurement);
        updatePreferenceSummary(speedUnitsOfMeasurement);
    }

        private void updatePreferenceSummary(Preference preference) {
            String code = getStoredValue(preference);
            updateSummary(preference, getDisplayValue(preference, code));
            preference.setOnPreferenceChangeListener(this);
        }

        private String getStoredValue(Preference preference) {
            // TODO set default value of units based on primary Locale
            String key = preference.getKey();
            return preference.getSharedPreferences().getString(key, null);
        }

        @Nullable
        private String getDisplayValue(Preference preference, String storedValue) {
            if (preference.equals(dataRefreshPeriod))
                return getDataRefreshPeriodDisplayValue(storedValue);
            if (preference.equals(temperatureUnitsOfMeasurement))
                return getTemperatureMeasurementDisplayValue(storedValue);
            if (preference.equals(speedUnitsOfMeasurement))
                return getSpeedMeasurementDisplayValue(storedValue);
            return null;
        }

            private String getTemperatureMeasurementDisplayValue(String storedValue) {
                return getDisplayValue(storedValue, temperatureMeasurementStoredValues,
                        temperatureMeasurementDisplayValues);
            }

            private String getSpeedMeasurementDisplayValue(String storedValue) {
                return getDisplayValue(storedValue, speedMeasurementStoredValues,
                        speedMeasurementDisplayValues);
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
                if (storedValue != null) {
                    if (storedValue.equals(storedValues[i]))
                        return displayValues[i];
                }
            }
            return null;
        }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.equals(dataRefreshPeriod))
            updateSummary(preference, getDataRefreshPeriodDisplayValue(o.toString()));
        if (preference.equals(temperatureUnitsOfMeasurement)) {
            setSpeedUnitsPreferenceBasedOnTempUnits(o.toString());
            updateSummary(preference, getTemperatureMeasurementDisplayValue(o.toString()));
        }
        if (preference.equals(speedUnitsOfMeasurement)) {
            saveLastUsedSpeedWithCelsius(o.toString());
            updateSummary(preference, getSpeedMeasurementDisplayValue(o.toString()));
        }

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
