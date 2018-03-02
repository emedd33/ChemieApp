package com.example.eskild.hc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Eskild on 28.02.2018.
 */

public class BackgroundWorker_kaffe extends AsyncTask<Void,Void,Integer> {
    public Context context;
    public int respons;
    @Override
    protected Integer doInBackground(Void... voids) {

        // R22
        String url_string = "http://10.22.27.117:8000/api/coffee/coffeesubmission/";

        //Fjordgata
        //String url_string = "http://192.168.20.2:8000/api/coffee/Coffeesubmission/";

        // Kristine
        //String url_string = "http://192.168.1.6:8000/api/coffee/Coffeesubmission/";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token = prefs.getString("token","");
        try {
            URL url = new URL(url_string);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setConnectTimeout(3000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", token);
            con.setDoOutput(true);

            OutputStream outputStream = con.getOutputStream();
            outputStream.close();
            respons = con.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respons;
    }

    BackgroundWorker_kaffe(Context context){
        this.context = context;
        this.respons = -1;
    }
}
