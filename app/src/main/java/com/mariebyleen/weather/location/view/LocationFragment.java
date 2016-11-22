package com.mariebyleen.weather.location.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariebyleen.weather.R;
import com.mariebyleen.weather.location.di.component.DaggerLocationComponent;
import com.mariebyleen.weather.location.di.module.LocationModule;
import com.mariebyleen.weather.location.presenter.LocationViewContract;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mariebyleen.weather.application.WeatherApplication.getAndroidComponent;

public class LocationFragment extends Fragment implements LocationViewContract {

    private ProgressDialog dialog;

    @Inject LocationPresenterContract presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        onCreateViewResolveDaggerDependency();
        return view;
    }

    private void onCreateViewResolveDaggerDependency() {
        DaggerLocationComponent.builder()
                .androidComponent(getAndroidComponent())
                .locationModule(new LocationModule(this, getContext()))
                .build().inject(this);
    }

    @OnClick(R.id.button_use_current_location)
    public void useCurrentLocation() {
        presenter.useCurrentLocation();
    }

    public void showProgressDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
        }
        dialog.setMessage(message);
        dialog.show();

    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }
}
