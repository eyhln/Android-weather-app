package com.mariebyleen.weather;

import android.support.v4.app.Fragment;

public class LocationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LocationFragment();
    }
}
