package com.mariebyleen.weather.preferences;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import javax.inject.Inject;

public class Preferences {

    private SharedPreferences preferences;
    private Resources resources;
    private Gson gson;

    private SharedPreferences.Editor editor;

    @Inject
    public Preferences (SharedPreferences preferences, Resources resources, Gson gson) {
        this.preferences = preferences;
        this.resources = resources;
        this.gson = gson;
        editor = preferences.edit();
    }

    private String getTag(int tagStringResource) {
        return resources.getString(tagStringResource);
    }

    public void putFloat(int tagStringResource, float value) {
        String tag = getTag(tagStringResource);
        editor.putFloat(tag, value);
        editor.apply();
    }

    public float getFloat(int tagStringResource, float defaultValue) {
        String tag = getTag(tagStringResource);
        return preferences.getFloat(tag, defaultValue);
    }


}
