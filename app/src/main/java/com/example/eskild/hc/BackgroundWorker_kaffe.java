package com.example.eskild.hc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.OutputStream;
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
    private ProgressDialog progressBar;

    @Override
    protected Integer doInBackground(Void... voids) {
        String url_string = "https://chemie.no/api/coffee/coffeesubmission/";

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
