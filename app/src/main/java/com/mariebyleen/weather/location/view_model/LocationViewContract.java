package com.mariebyleen.weather.location.view_model;

import android.database.Cursor;

public interface LocationViewContract  {

    void checkPermissions();
    void disableUseCurrentLocationOption();
    void showLocationSuggestions(String[] suggestions);
    String getSearchTextViewText();
    void navigateToMainActivity();
    void showCouldNotGetDataErrorMessage();
    void displayRecentLocationsData(Cursor cursor);

}
