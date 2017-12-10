package com.example.eskild.hc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Sladreboksen_activity extends AppCompatActivity implements View.OnClickListener {
    public Toolbar toolbar;
    public static final int GET_FROM_GALLERY = 3;
    public TextView filename;
    public String file_string;
    public String sladder_text;
    public Bitmap bitmap;
    public ProgressBar progress_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sladreboksen_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress_send = (ProgressBar)findViewById(R.id.Progress_send);
        progress_send.setVisibility(View.INVISIBLE);
        filename = (TextView)findViewById(R.id.filename);
        //setter onclick upload på id\sladre_button
        //onClick lar bruker velge filer fra gallery til upload
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:

                EditText editText = (EditText)findViewById(R.id.Sladder_text);
                sladder_text = editText.getText().toString();

                BackgroundWorker backgroundWorker = new BackgroundWorker(this,progress_send);
                try {
                    String result = backgroundWorker.execute("send_sladder", sladder_text).get();
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    editText.setText("");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(Sladreboksen_activity.this, HovedActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Toast.makeText(Sladreboksen_activity.this, "Profile", Toast.LENGTH_SHORT).show();
                Intent profileintent = new Intent(this, myProfile.class);
                startActivity(profileintent);
                return true;
            case R.id.Settings:
                Toast.makeText(Sladreboksen_activity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Sladreboksen_activity.this, HovedActivity.class);
        startActivity(intent);
    }
    public void onSladreButtonSlick(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //henter ut image fra files,
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_FROM_GALLERY);
        //mangler å vise filename i activity.
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            file_string = selectedImage.getLastPathSegment();

            filename.setText(file_string);
            selectedImage.getEncodedAuthority();
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                filename.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


