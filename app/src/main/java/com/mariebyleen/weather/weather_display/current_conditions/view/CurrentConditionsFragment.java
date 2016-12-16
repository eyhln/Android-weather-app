package com.mariebyleen.weather.weather_display.current_conditions.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.databinding.FragmentCurrentConditionsBinding;
import com.mariebyleen.weather.navigation.Navigator;
import com.mariebyleen.weather.weather_display.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.weather_display.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class CurrentConditionsFragment extends Fragment {

    @Inject
    CurrentConditionsViewModel viewModel;
    @Inject
    Navigator navigator;

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
    public void onDestroy() {
        super.onDestroy();
        viewModel.onViewDestroy();
    }
}
