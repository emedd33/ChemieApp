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

        String url_string = "https://chemie.no/api/profile/profile/";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token = prefs.getString("token","");
        try {

            URL url = new URL(url_string);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");

            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            JsonReader jsonReader = new JsonReader(inputStreamReader);

            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                setUserInfoFromJson(jsonReader,prefs); // her henter jeg ut data fra JSON og legger dem i session
            }
            jsonReader.close();
            inputStreamReader.close();

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
        jsonReader.beginObject();
        while ( jsonReader.hasNext()){
            String name =jsonReader.nextName();

            if (name.equals("access_card")) {

                String access_card = jsonReader.nextString();
                prefs.edit().putString("access_card", access_card).commit();

            } else if (name.equals("image_primary")) {
                String url = jsonReader.nextString();
                Bitmap image = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                prefs.edit().putString("image",encodedImage).commit();

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
