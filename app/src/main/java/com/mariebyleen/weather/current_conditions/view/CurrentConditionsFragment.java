package com.mariebyleen.weather.current_conditions.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.current_conditions.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.current_conditions.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.databinding.FragmentCurrentConditionsBinding;
import com.mariebyleen.weather.navigation.Navigator;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class CurrentConditionsFragment extends Fragment {

    @Inject
    CurrentConditionsViewModel viewModel;
    @Inject
    Navigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        onCreateViewResolveDaggerDependency();

        FragmentCurrentConditionsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_current_conditions, container, false);
        binding.setConditions(viewModel);

        viewModel.onViewCreate();

        return binding.getRoot();
    }


    private void onCreateViewResolveDaggerDependency() {
        DaggerCurrentConditionsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .currentConditionsModule(new CurrentConditionsModule())
                .build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onViewPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                navigator.navigateToSettings(getContext());
                return true;
            default:
                super.onOptionsItemSelected(item);
                return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onViewDestroy();
    }
}
