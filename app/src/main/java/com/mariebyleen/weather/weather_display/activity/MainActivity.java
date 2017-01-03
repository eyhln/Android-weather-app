package com.mariebyleen.weather.weather_display.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.base.BaseActivity;
import com.mariebyleen.weather.databinding.ActivityMainBinding;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.weather_display.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.weather_display.job.WeatherDataService;
import com.mariebyleen.weather.weather_display.view.CurrentConditionsViewModel;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class MainActivity extends BaseActivity {

    private final int DETAIL_VIEW_HEIGHT = 373;

    @Inject
    WeatherDataService weatherDataService;
    @Inject
    CurrentConditionsViewModel viewModel;
    @Inject
    Navigator navigator;
    @Inject
    ForecastRecyclerAdapter adapter;

    @BindView(R.id.container_layout)
    LinearLayout container;

    @BindView(R.id.button_expand_collapse)
    @Nullable
    ToggleButton button;
    @BindView(R.id.current_conditions_detail_content)
    GridLayout detailContent;
    @BindDrawable(R.drawable.ic_expand_more_black_24dp)
    Drawable expandMore;
    @BindDrawable(R.drawable.ic_expand_less_black_24dp)
    Drawable expandLess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateResolveDaggerDependency();
        onCreateSetupDataBinding();
        ButterKnife.bind(this);
        onCreateSetupToolbar();
        onCreateSetupDetailView();
        weatherDataService.manageJobRequests();
    }

    private void onCreateSetupToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void onCreateResolveDaggerDependency() {
        DaggerCurrentConditionsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .currentConditionsModule(new CurrentConditionsModule())
                .build().inject(this);
    }

    private void onCreateSetupDataBinding() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setConditions(viewModel);
        viewModel.onViewCreate();
    }

    private void onCreateSetupDetailView() {
        if (button != null)
            button.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        onResumeSetupRecyclerView();
        super.onResume();
        viewModel.onViewResume();
    }

    private void onResumeSetupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_forecast);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Optional
    @OnClick(R.id.button_expand_collapse)
    public void showAndHideDetailContent() {
        if (button != null) {
            if (button.isChecked())
                animateDetailContent(DETAIL_VIEW_HEIGHT, 0);
            else
                animateDetailContent(0, DETAIL_VIEW_HEIGHT);
        }
    }

    private void animateDetailContent(int startSize, int endSize) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(detailContent, startSize, endSize);
        resizeAnimation.setDuration(300);
        detailContent.startAnimation(resizeAnimation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onViewDestroy();
    }
}
