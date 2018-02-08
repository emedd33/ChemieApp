package com.example.eskild.hc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.provider.Contacts.SettingsColumns.KEY;

public class HovedActivity extends AppCompatActivity implements  View.OnClickListener {
    public static final String TAG = "MyActivity";
    private Toolbar toolbar;
    private String Token;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoved);

        Intent i = getIntent();
        this.session = getIntent().getExtras().getParcelable("session");
        //this.session = i.getParcelableExtra("session");


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
        Intent intent_pre = getIntent();
        this.session = intent_pre.getParcelableExtra("session");
        switch (id) {
            case R.id.profile_image:
                Toast.makeText(HovedActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, myProfile.class);
                intent.putExtra("session",this.session);
                startActivity(intent);
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
        Intent intent_pre = getIntent();
        this.session=intent_pre.getParcelableExtra("session");
        switch (v.getId()) {
            case R.id.sladreboksen_button:{
                Intent intent = new Intent(HovedActivity.this, Sladreboksen_activity.class);
                intent.putExtra("session",this.session);
                Toast.makeText(HovedActivity.this, "Sladreboksen", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.cake_button:{
                Intent cake_intent = new Intent(HovedActivity.this, kaffeknappen_activity.class);
                cake_intent.putExtra("session",this.session);

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