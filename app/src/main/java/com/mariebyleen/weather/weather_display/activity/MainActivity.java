package com.mariebyleen.weather.weather_display.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.weather_display.current_conditions.view.CurrentConditionsFragment;
import com.mariebyleen.weather.weather_display.forecast.view.ForecastFragment;
import com.mariebyleen.weather.weather_display.job.WeatherDataService;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager_weather_display)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_weather_display)
    TabLayout tabLayout;

    @BindString(R.string.current_conditions)
    String currentConditions;
    @BindString(R.string.forecast)
    String forecast;

    @Inject
    WeatherDataService weatherDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        onCreateSetupViewPager();
        getApplicationComponent().inject(this);
        weatherDataService.manageJobRequests();
    }

    private void onCreateSetupViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0)
                    return new CurrentConditionsFragment();
                if (position == 1)
                    return new ForecastFragment();
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0)
                    return currentConditions;
                if (position == 1)
                    return forecast;
                return super.getPageTitle(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Navigator navigator = new Navigator();
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                navigator.navigateToPreferences(this);
                return true;
            case R.id.menu_item_location:
                navigator.navigateToLocationEditor(this);
            case R.id.menu_item_update:
                weatherDataService.scheduleOneOffUpdate();
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }
}
