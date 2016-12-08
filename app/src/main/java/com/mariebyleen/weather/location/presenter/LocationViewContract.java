package com.mariebyleen.weather.location.presenter;

public interface LocationViewContract  {

    void showProgressDialog(String message);
    void hideProgressDialog();
    void checkPermissions();
    void disableUseCurrentLocationOption();

}
