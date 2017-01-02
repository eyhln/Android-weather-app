package com.mariebyleen.weather.weather_display.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.databinding.ActivityMainBinding;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.weather_display.job.WeatherDataService;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class MainActivity extends BaseActivity {

    @Inject
    WeatherDataService weatherDataService;
    @Inject
    CurrentConditionsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateResolveDaggerDependency();
        onCreateSetupDataBinding();
        weatherDataService.manageJobRequests();
    }

    private void onCreateSetupDataBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setConditions(viewModel);
        viewModel.onViewCreate();
    }

    private void onCreateResolveDaggerDependency() {
        DaggerCurrentConditionsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .currentConditionsModule(new CurrentConditionsModule())
                .build().inject(this);
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

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onViewResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onViewDestroy();
    }
}
