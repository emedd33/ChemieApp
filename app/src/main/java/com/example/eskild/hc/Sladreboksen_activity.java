package com.example.eskild.hc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Sladreboksen_activity extends AppCompatActivity implements View.OnClickListener {
    public Toolbar toolbar;
    public static final int GET_FROM_GALLERY = 3;
    public String filename_string;
    public String sladder_text;
    public Bitmap bitmap;
    public ProgressBar progress_send;
    public EditText sladder_edittext;
    private TextView filename;
    private Button delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sladreboksen);
        this.sladder_edittext = (EditText)findViewById(R.id.Sladder_text);
        this.bitmap = null;

        Button delete_button = (Button)findViewById(R.id.button_delete_image);
        this.delete_button = delete_button;
        delete_button.setEnabled(false);
        delete_button.setVisibility(View.INVISIBLE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setter onclick upload på id\sladre_button
        //onClick lar bruker velge filer fra gallery til upload*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Sladreboksen_activity.this, HovedActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        MyTaskParams myTaskParams = new MyTaskParams();
        TextView filename = (TextView)findViewById(R.id.filename);
        this.filename = filename;

        switch (v.getId()){
            case R.id.send_button:
                myTaskParams.request = "SEND_SLADDER";
                sladder_text = this.sladder_edittext.getText().toString();
                myTaskParams.content = sladder_text;

                BackgroundWorker_sladder Worker = new BackgroundWorker_sladder(this,progress_send);
                if (sladder_text.equals("") && (this.bitmap != null)){
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
                        filename.setVisibility(View.INVISIBLE);
                        sladder_edittext.setText("");
                        delete_button.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(this, "Error Response code:" + result, Toast.LENGTH_SHORT).show();
                    }
                        this.sladder_edittext.setText("");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                break;
            case R.id.image_buton:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //henter ut image fra files,
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_FROM_GALLERY);
                break;

            case R.id.Sladder_text:
                this.sladder_edittext.setFocusable(true);
                break;
            case R.id.button_delete_image:
                this.bitmap = null;
                filename.setText("");
                filename.setVisibility(View.INVISIBLE);
                delete_button.setVisibility(View.INVISIBLE);
                delete_button.setEnabled(false);

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(Sladreboksen_activity.this, HovedActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile_image:
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String filename_string = selectedImage.getLastPathSegment();
            this.filename_string = filename_string;
            selectedImage.getEncodedAuthority();
            try {
                Bitmap upload = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                this.bitmap = upload;
                filename.setText("Ready to upload");
                filename.setVisibility(View.VISIBLE);
                delete_button.setEnabled(true);
                delete_button.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MyTaskParams {
        public String request;
        public String content;
        public String String_image;
        private String token;


        MyTaskParams(){
            this.request = null;
            this.content = null;
            this.String_image = null;
            this.token = null;
        }
        private void setToken(String token){
            this.token = token;
        }
        public String getToken() {return this.token;}

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


}



