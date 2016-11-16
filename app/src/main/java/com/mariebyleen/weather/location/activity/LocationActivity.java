package com.mariebyleen.weather.location.activity;

import android.support.v4.app.Fragment;

import com.mariebyleen.weather.base.SingleFragmentActivity;
import com.mariebyleen.weather.location.view.LocationFragment;

public class LocationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LocationFragment();
    }

}
