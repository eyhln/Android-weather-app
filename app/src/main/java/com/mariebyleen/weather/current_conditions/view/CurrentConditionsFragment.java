package com.mariebyleen.weather.current_conditions.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.current_conditions.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.current_conditions.di.module.CurrentConditionsModule;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.databinding.FragmentCurrentConditionsBinding;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getNetworkComponent;

public class CurrentConditionsFragment extends Fragment {

    @Inject
    CurrentConditionsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_conditions, container, false);
        onCreateViewResolveDaggerDependency();
        FragmentCurrentConditionsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_current_conditions, container, false);
        binding.setConditions(viewModel);
        return view;
    }

    private void onCreateViewResolveDaggerDependency() {
        DaggerCurrentConditionsComponent.builder()
                .networkComponent(getNetworkComponent())
                .currentConditionsModule(new CurrentConditionsModule())
                .build().inject(this);
    }
}
