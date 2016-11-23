package com.mariebyleen.weather.activity;

import android.support.v4.app.Fragment;

import com.mariebyleen.weather.base.SingleFragmentActivity;
import com.mariebyleen.weather.current_conditions.view.CurrentConditionsFragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CurrentConditionsFragment();
    }
}
