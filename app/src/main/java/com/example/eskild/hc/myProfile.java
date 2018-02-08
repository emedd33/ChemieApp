package com.example.eskild.hc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class myProfile extends AppCompatActivity {
    Toolbar toolbar1;
    Session session;
    Button logout_button;
    Bitmap profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar1 = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.logout_button = (Button)findViewById(R.id.button_logout);

        Intent i = getIntent();
        this.session = i.getParcelableExtra("session");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        session.setPrefs(prefs);
        this.profile_image = session.DecodedImage(prefs.getString("image",""));
        this.setInfo();


    }
    public void onClick(View view){
         switch (view.getId()){
             case R.id.button_logout:
                 session.Logout();
                 Intent intent = new Intent(this,LoginActivity.class);
                 startActivity(intent);
                 break;
         }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, HovedActivity.class);
                startActivity(intent);
                return true;
            case R.id.Settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, HovedActivity.class);
        startActivity(intent);
    }

    public void setInfo() {

        ImageView img = (ImageView)findViewById(R.id.profile_image);
        Drawable d = new BitmapDrawable(getResources(),this.profile_image);
        img.setImageDrawable(d);

        TextView email = (TextView)findViewById(R.id.email_edit);
        TextView access_card = (TextView)findViewById(R.id.acces_card_edit);
        TextView telefon = (TextView)findViewById(R.id.telefon_edit);
        TextView allergi = (TextView)findViewById(R.id.allergier_edit);

        email.setText(session.getusename());
        access_card.setText(session.getAcces_card());
        telefon.setText(String.valueOf(session.getTelefon()));
        allergi.setText(session.getAllergier());


    }
}
