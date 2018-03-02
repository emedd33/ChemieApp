package com.example.eskild.hc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    Context contex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contex = this;
        this.prefs  = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("Access",false)){
            Intent intent = new Intent(this, HovedActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
