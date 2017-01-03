package com.mariebyleen.weather.location.activity;

public interface LocationViewContract  {

    void showProgressDialog(String message);
    void hideProgressDialog();
    void checkPermissions();
    void disableUseCurrentLocationOption();

}
