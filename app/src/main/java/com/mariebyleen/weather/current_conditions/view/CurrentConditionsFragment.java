package com.mariebyleen.weather.current_conditions.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.current_conditions.view_model.CurrentConditionsViewModel;
import com.mariebyleen.weather.databinding.FragmentCurrentConditionsBinding;

public class CurrentConditionsFragment extends Fragment {

    private CurrentConditionsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_conditions, container, false);

        viewModel = new CurrentConditionsViewModel();
        FragmentCurrentConditionsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_current_conditions, container, false);
        binding.setConditions(viewModel);
        return view;
    }
}
