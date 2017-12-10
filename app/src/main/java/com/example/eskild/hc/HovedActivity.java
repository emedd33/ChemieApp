package com.example.eskild.hc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HovedActivity extends AppCompatActivity implements  View.OnClickListener {
    public static final String TAG = "MyActivity";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoved);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "index=");
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                Toast.makeText(HovedActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                Intent profileintent = new Intent(this, myProfile.class);
                startActivity(profileintent);
                return true;

            case R.id.Settings:
                Toast.makeText(HovedActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sladreboksen_button:{
                Intent sladre_intent = new Intent(HovedActivity.this, Sladreboksen_activity.class);
                Toast.makeText(HovedActivity.this, "Sladreboksen", Toast.LENGTH_SHORT).show();
                startActivity(sladre_intent);
                break;
            }
            case R.id.cake_button:{
                Intent cake_intent = new Intent(HovedActivity.this, kaffeknappen_activity.class);
                Toast.makeText(HovedActivity.this, "Kake- og kaffekknappen", Toast.LENGTH_SHORT).show();
                startActivity(cake_intent);
                break;
            }
            case R.id.event_button:{
                Toast.makeText(HovedActivity.this, "Events og bedpress", Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }
}