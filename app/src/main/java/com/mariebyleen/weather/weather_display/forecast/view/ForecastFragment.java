package com.mariebyleen.weather.weather_display.forecast.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.weather_display.di.component.DaggerCurrentConditionsComponent;
import com.mariebyleen.weather.weather_display.di.module.CurrentConditionsModule;

import javax.inject.Inject;

import static com.mariebyleen.weather.application.WeatherApplication.getApplicationComponent;

public class ForecastFragment extends Fragment {

    @Inject
    ForecastRecyclerAdapter adapter;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        onCreateViewResolveDaggerDependency();
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    private void onCreateViewResolveDaggerDependency() {
        DaggerCurrentConditionsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .currentConditionsModule(new CurrentConditionsModule())
                .build().inject(this);
    }

    @Override
    public void onResume() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_forecast);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        super.onResume();
    }
}
