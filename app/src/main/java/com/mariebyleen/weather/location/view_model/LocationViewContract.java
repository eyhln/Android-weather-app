package com.mariebyleen.weather.location.view_model;

public interface LocationViewContract  {

    void checkPermissions();
    void disableUseCurrentLocationOption();
    void showLocationSuggestions(String[] suggestions);
    String getSearchTextViewText();
    void navigateToMainActivity();
    void showCouldNotGetDataErrorMessage();

}
