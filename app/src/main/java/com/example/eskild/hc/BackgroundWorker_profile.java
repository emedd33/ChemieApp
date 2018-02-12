package com.example.eskild.hc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.JsonReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskild on 29.01.2018.
 */

public class BackgroundWorker_profile extends AsyncTask<Void, Void, Void> {
    Context context;
    private ProgressDialog progressBar;

    public BackgroundWorker_profile(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {
        // ipv4 for Fjordgata 17
        //String url_string = "http://192.168.20.4:8000/api/profile/profile/";

        // Lesesalen
        String url_string = "http://10.22.24.171:8000/api/profile/profile/";

        // url for nettsidene
        // String url_string = "https://chemie.no/api/profile/profile/";

        // Hos Kristine
        //String url_string = "http://192.168.1.6:8000/api/profile/profile/";

        // K26
        //String url_string = "http://10.22.8.81:8000/api/profile/profile/";

        // R21
        //String url_string = "http://10.22.11.147:8000/api/profile/profile/";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token = prefs.getString("token","");
        URL url = null;
        try {

            url = new URL(url_string);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            JsonReader jsonReader = new JsonReader(inputStreamReader);

            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                setUserInfoFromJson(jsonReader,prefs);// her henter jeg ut data fra JSON og legger dem i session
            }
            jsonReader.close();
            inputStreamReader.close();
            int respons = con.getResponseCode();
            if (respons== 200){
                prefs.edit().putBoolean("Access",true).commit();
            }

        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void setUserInfoFromJson(JsonReader jsonReader, SharedPreferences prefs) throws IOException {
        String acces_card = "none";
        int phone_number = -1;
        String allergies = "none";
        jsonReader.beginObject();
        while ( jsonReader.hasNext()){
            String name =jsonReader.nextName();
            if (name.equals("phone_number")){
                phone_number = jsonReader.nextInt();
                prefs.edit().putInt("phone_number", phone_number).commit();

            } else if (name.equals("access_card")) {
                String access_card = jsonReader.nextString();
                prefs.edit().putString("access_card", access_card).commit();

            } else if (name.equals("image_primary")) {
                String url = jsonReader.nextString();
                InputStream is = (InputStream) new URL(url).getContent();
                Bitmap image = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                prefs.edit().putString("image",encodedImage).commit();

            } else if (name.equals("allergies")){
                allergies = jsonReader.nextString();
                prefs.edit().putString("allergies", allergies).commit();

            } else {
                jsonReader.skipValue();
            }
        }
    }

    @Override
    protected void onPreExecute(){
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage("Login in...");
        progressBar.show();
    }


    public Bitmap DecodedImage(String encodedImage, SharedPreferences prefs) {
        String previouslyEncodedImage = prefs.getString("image", "");
        Bitmap bitmap = null;
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }
}
