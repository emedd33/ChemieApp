package com.example.eskild.hc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.JsonReader;

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

public class BackgroundWorker_profile extends AsyncTask<Session, Void, Integer> {
    Context context;
    public BackgroundWorker_profile(Session session) {
        context = session.getContext();
    }


    @Override
    protected Integer doInBackground(Session... params) {
        Session session = params[0];

        // ipv4 for Fjordgata 17
        String url_string = "http://192.168.20.4:8000/api/profile/profile/";

        // Lesesalen
        //String url_string = "http://10.22.26.196:8000/api/profile/profile/";

        // url for nettsidene
        // String url_string = "https://chemie.no/api/profile/profile/";

        // K26
        //String url_string = "http://10.22.8.81:8000/api/profile/profile/";

        // R21
        //String url_string = "http://10.22.11.147:8000/api/profile/profile/";

        String token = session.getToken();
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
            while (jsonReader.hasNext()) {
                setUserInfoFromJson(jsonReader,session); // her henter jeg ut data fra JSON og legger dem i session
            }
            jsonReader.endArray();
            inputStreamReader.close();
            int respons = con.getResponseCode();
            if (respons== 200){
                session.setAccess(true);
            }
            return respons;
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void setUserInfoFromJson(JsonReader jsonReader,Session session) throws IOException {
        String acces_card = "none";
        int phone_number = -1;
        String allergies = "none";
        jsonReader.beginObject();
        while ( jsonReader.hasNext()){
            String name =jsonReader.nextName();
            if (name.equals("phone_number")){
                phone_number = jsonReader.nextInt();
                session.setPhoneNumber(phone_number);

            } else if (name.equals("access_card")) {
                String access_card = jsonReader.nextString();
                session.setAccesCard(acces_card);

            } else if (name.equals("image_primary")) {
                String url = jsonReader.nextString();
                InputStream is = (InputStream) new URL(url).getContent();
                Bitmap image = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
                session.setImage(image);

            } else if (name.equals("allergies")){
                allergies = jsonReader.nextString();
                session.setAllergies(allergies);
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
    }
}
