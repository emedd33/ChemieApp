package com.example.eskild.hc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Eskild on 21.01.2018.
 */

public class BackgroundWorker_sladder extends AsyncTask<HovedActivity.MyTaskParams, Void, String> {
    private final SharedPreferences prefs;
    private Context context;
    private ProgressBar progressbar;
    public String respons;



    @Override
    protected String doInBackground(HovedActivity.MyTaskParams... params) {
        HovedActivity.MyTaskParams myTaskparams = params[0];

             //Fjordgata 17 ipv4
            String url_string = "http://192.168.20.2:8000/api/sladreboks/submission/";

            // url til chemie.no
            //String url_string = "https://chemie.no/api/sladreboks/submission/";

            //String url_string = "http://10.22.8.81:8000/api/sladreboks/submission/";


            String token = this.prefs.getString("token","");

            try {
                URL url = new URL(url_string);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", myTaskparams.content);
                jsonObject.put("image", myTaskparams.String_image);
                String JSON = jsonObject.toString();

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setRequestMethod("POST");

                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", token);
                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(JSON);
                writer.close();
                outputStream.close();

                respons = String.valueOf(con.getResponseCode());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return respons;
        }

    public BackgroundWorker_sladder(Context con, SharedPreferences prefs) {
        this.context = con;
        this.prefs = prefs;
        this.respons = "500";
    }

}
