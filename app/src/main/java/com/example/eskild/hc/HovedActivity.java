package com.example.eskild.hc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;



public class HovedActivity extends AppCompatActivity implements  View.OnClickListener {
    public static final int GET_FROM_GALLERY = 3;
    private Toolbar toolbar;
    private EditText sladder_edittext;
    private String sladder_string;
    private Bitmap bitmap;
    private SharedPreferences prefs;
    private TextView filename;
    private ImageButton delete_btn;
    private ImageButton addPhoto_btn;
    private ImageButton kaffe_btn;
    private Button send_btn;
    private ConstraintLayout file_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoved);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        this.sladder_edittext = (EditText)findViewById(R.id.sladder_text);
        this.filename = (TextView)findViewById(R.id.filename_view);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.delete_btn = (ImageButton)findViewById(R.id.delete_btn);
        this.addPhoto_btn = (ImageButton)findViewById(R.id.addPhoto_btn);
        this.kaffe_btn = (ImageButton)findViewById(R.id.kaffe_btn);
        this.send_btn = (Button)findViewById(R.id.send_btn);
        this.file_layout = (ConstraintLayout)findViewById(R.id.file_layout);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Settings:
                Toast.makeText(HovedActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, myProfile.class);
                startActivity(intent);
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
        //her gjemmes tastaturen for brukeren.
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        switch (v.getId()) {
            case R.id.addPhoto_btn:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //henter ut image fra files,
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_FROM_GALLERY);
                break;
            case R.id.delete_btn:
                this.bitmap = null;
                this.filename.setVisibility(View.INVISIBLE);
                this.delete_btn.setVisibility(View.INVISIBLE);
                this.delete_btn.setEnabled(false);
                this.file_layout.setVisibility(View.INVISIBLE);
                break;
            case R.id.send_btn:
                sladder_string = this.sladder_edittext.getText().toString();
                if (sladder_string.equals("") && (this.bitmap == null)){
                    break;
                }
                sendSladder();
                break;
            case R.id.kaffe_btn:
                BackgroundWorker_kaffe worker = new BackgroundWorker_kaffe(HovedActivity.this);
                worker.execute();
                try {
                    int respons = worker.get();
                    if (respons == 201){
                        Toast.makeText(HovedActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HovedActivity.this, "Respons Error: " + String.valueOf(respons), Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    class MyTaskParams {
        public String content;
        public String String_image;

        MyTaskParams(){
            this.content = null;
            this.String_image = null;
        }

        private String convertBitmap(Bitmap bitmapPicture) {
            final int COMPRESSION_QUALITY = 100;
            String encodedImage;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                    byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
    }

    private void sendSladder() {
        MyTaskParams myTaskParams = new MyTaskParams();
        myTaskParams.content = sladder_string;

        BackgroundWorker_sladder Worker = new BackgroundWorker_sladder(this, this.prefs);
        if (sladder_string.equals("") && (this.bitmap != null)){
            myTaskParams.content = "Image upload";
        }
        try {
            if (this.bitmap!= null){
                String String_image = myTaskParams.convertBitmap(this.bitmap);
                myTaskParams.String_image = String_image;
            }
            String result = Worker.execute(myTaskParams).get();
            if (result.equals("201")){
                Toast.makeText(this, "Sladder sendt", Toast.LENGTH_SHORT).show();
                filename.setText("");
                sladder_string = null;
                filename.setVisibility(View.INVISIBLE);
                sladder_edittext.setText("");
                delete_btn.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(this, "Error Response code:" + result, Toast.LENGTH_SHORT).show();
            }
            this.sladder_edittext.setText("");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            selectedImage.getEncodedAuthority();
            try {
                Bitmap upload = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                this.bitmap = upload;
                this.filename.setVisibility(View.VISIBLE);
                this.delete_btn.setEnabled(true);
                this.delete_btn.setVisibility(View.VISIBLE);
                this.file_layout.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}