package com.chornij.finances;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;


public class SettingsActivity extends Activity {

    public static final String SETTINGS_NAME = "settings";

    EditText etPrimaryServer;
    EditText etSecondaryServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPrimaryServer = (EditText)findViewById(R.id.primaryServer);
        etSecondaryServer = (EditText)findViewById(R.id.secondaryServer);

        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);

        etPrimaryServer.setText(settings.getString("primaryServer", ""));
        etSecondaryServer.setText(settings.getString("secondaryServer", ""));
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("primaryServer", etPrimaryServer.getText().toString());
        editor.putString("secondaryServer", etSecondaryServer.getText().toString());

        editor.apply();
    }

}
