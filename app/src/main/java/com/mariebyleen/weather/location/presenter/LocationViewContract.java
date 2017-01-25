package com.mariebyleen.weather.location.presenter;

public interface LocationViewContract  {

    boolean permissionsGiven();
    void requestPermissions();
    void disableUseCurrentLocationOption();
    void showLocationSuggestions(String[] suggestions);
    String getSearchTextViewText();
    void navigateToMainActivity();
    void enableSearchLocationSelection(boolean enable);
    void enableUseRecentLocationSelection(boolean enable);
    void showNoAccessToWeatherServiceErrorMessage();

}
