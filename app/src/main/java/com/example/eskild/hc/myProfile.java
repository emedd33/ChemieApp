package com.example.eskild.hc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.TextView;
import android.widget.Toast;

public class myProfile extends AppCompatActivity {
    Toolbar toolbar1;
    String allegier;
    String access_card;
    int phone_number;
    Button logout_button;
    Bitmap profile_image;
    private ImageView img;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedInstanceState==null){
            setInfo(prefs);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("image",this.profile_image);
        savedInstanceState.putString("access_card",this.access_card);
        savedInstanceState.putString("allergies",this.allegier);
        savedInstanceState.putInt("phone_number",this.phone_number);
        savedInstanceState.putBoolean("saved",true);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.phone_number = savedInstanceState.getInt("phone_number");
        this.allegier = savedInstanceState.getString("allergies");
        this.access_card = savedInstanceState.getString("access_card");
        this.profile_image = savedInstanceState.getParcelable("image");
    }

    public void onClick(View view){
         switch (view.getId()){
             case R.id.button_logout:
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

    public void setInfo(SharedPreferences prefs) {


        this.img = (ImageView)findViewById(R.id.profile_image);
        String image_string = prefs.getString("image","");
        if (!image_string.equals("")){
            Bitmap image = DecodedImage(image_string,prefs);
            Drawable d = new BitmapDrawable(getResources(), image);
            this.img.setImageDrawable(d);
        }

        TextView access_card = (TextView)findViewById(R.id.acces_card_edit);
        TextView telefon = (TextView)findViewById(R.id.telefon_edit);
        TextView allergi = (TextView)findViewById(R.id.allergier_edit);

        this.allegier = prefs.getString("allergies","");
        this.access_card = prefs.getString("access_card","");
        this.phone_number = prefs.getInt("phone_number",22225555);

        access_card.setText(this.access_card);
        telefon.setText(String.valueOf(this.phone_number));
        allergi.setText(this.allegier);


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
