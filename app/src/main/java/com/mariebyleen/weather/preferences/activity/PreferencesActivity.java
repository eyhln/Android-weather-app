package com.mariebyleen.weather.preferences.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mariebyleen.weather.preferences.view.PreferencesFragment;

public class PreferencesActivity extends AppCompatActivity {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PreferencesActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new PreferencesFragment())
                    .commit();
        }
    }
}


