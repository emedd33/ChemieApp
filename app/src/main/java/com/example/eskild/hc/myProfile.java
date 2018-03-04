package com.example.eskild.hc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.messaging.FirebaseMessaging;

public class myProfile extends AppCompatActivity {
    Toolbar toolbar1;
    String access_card;
    Button logout_button;
    private ImageView img_view;
    private SharedPreferences prefs;
    public ToggleButton switch_kaffe;
    public ToggleButton switch_event;
    public ToggleButton switch_felles;
    private String brukernavn;
    private TextView brukernavn_edit;
    private TextView access_card_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);
        toolbar1 = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.brukernavn_edit = (TextView)findViewById(R.id.username_edit);
        this.img_view = (ImageView)findViewById(R.id.profile_image);
        this.access_card_edit = (TextView)findViewById(R.id.acces_card_edit);

        this.switch_event = (ToggleButton)findViewById(R.id.event_noti_btn);
        this.switch_kaffe = (ToggleButton)findViewById(R.id.kaffe_noti_btn);
        this.switch_felles = (ToggleButton)findViewById(R.id.felles_noti_btn);

        this.logout_button = (Button)findViewById(R.id.button_logout);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myProfile.this);
        this.prefs = prefs;


        setInfo(prefs);
    }


    public void onClick(View view){
         switch (view.getId()){
             case R.id.button_logout:
                 prefs.edit().clear().commit();
                 FirebaseMessaging.getInstance().unsubscribeFromTopic("Kaffe");
                 FirebaseMessaging.getInstance().unsubscribeFromTopic("Event");
                 FirebaseMessaging.getInstance().unsubscribeFromTopic("Felles");
                 Intent intent = new Intent(this,LoginActivity.class);
                 startActivity(intent);
                 break;

             case R.id.event_noti_btn:
                 if (this.prefs.getBoolean("Event",false)){
                     prefs.edit().putBoolean("Event",false).commit();
                     FirebaseMessaging.getInstance().unsubscribeFromTopic("Event");
                 } else {
                     prefs.edit().putBoolean("Event",true).commit();
                     FirebaseMessaging.getInstance().subscribeToTopic("Event");
                 }
                 break;
             case R.id.kaffe_noti_btn:
                 if (this.prefs.getBoolean("Kaffe",false)){
                     prefs.edit().putBoolean("Kaffe",false).commit();
                     FirebaseMessaging.getInstance().unsubscribeFromTopic("Kaffe");
                 } else {
                     prefs.edit().putBoolean("Kaffe",true).commit();
                     FirebaseMessaging.getInstance().subscribeToTopic("Kaffe");
                 }
                 break;

             case R.id.felles_noti_btn:
                 if (prefs.getBoolean("Felles",false)){
                     this.prefs.edit().putBoolean("Felles",false).commit();
                     FirebaseMessaging.getInstance().unsubscribeFromTopic("Felles");
                 } else {
                     prefs.edit().putBoolean("Felles",true).commit();
                     FirebaseMessaging.getInstance().subscribeToTopic("Felles");
                 }
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

    public void setInfo(SharedPreferences prefs) {

        String image_string = prefs.getString("image","");
        if (!image_string.equals("")){
            Bitmap image = DecodedImage(image_string,prefs);
            Drawable d = new BitmapDrawable(getResources(), image);
            this.img_view.setImageDrawable(d);
        }

        this.switch_event.setChecked(prefs.getBoolean("Event",false));
        this.switch_kaffe.setChecked(prefs.getBoolean("Kaffe",false));
        this.switch_felles.setChecked(prefs.getBoolean("Felles",false));

        this.access_card = prefs.getString("access_card","");
        this.brukernavn = prefs.getString("brukernavn","");


        access_card_edit.setText(this.access_card);
        brukernavn_edit.setText(this.brukernavn);


    }
    public Bitmap DecodedImage(String encodedImage,SharedPreferences prefs) {
        String previouslyEncodedImage = prefs.getString("image", "");
        Bitmap bitmap = null;
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }
}
