package com.example.eskild.hc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/*
 * Created by Eskild on 29.10.2017.
 */

public class BackgroundWorker_login extends AsyncTask<String, Void, Integer> {
    private Context context;
    ProgressDialog progressBar;

    BackgroundWorker_login(Context con){
        this.context = con;
    }

    @Override
    protected Integer doInBackground(String... params) {

        //Fjordgata 17 ipv4
        //String login_url = "http://192.168.20.4:8000/api/api-auth/";

        // Lesesal
        //String login_url = "http://10.22.26.196:8000/api/api-auth/";

        // Hos Kristine
        String login_url = "http://192.168.1.6:8000/api/api-auth/";

        // Chemie.no url
        //String login_url = "https://chemie.no/api/api-auth/";

        //K26
        //String login_url = "http://10.22.8.81:8000/api/api-auth/";

        // R21
        //String login_url = "http://10.22.11.147:8000/api/api-auth/";
        int respons = 500;
        String user_name = params[0];
        String password = params[1];
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            URL url = new URL(login_url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user_name);
            jsonObject.put("password", password);


            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream outputStream = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(String.valueOf(jsonObject));

            writer.close();
            outputStream.close();
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            String token = null;
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String token_pre = response.toString();
                token = "token " + token_pre.substring(10, token_pre.length() - 2);
                prefs.edit().putString("token",token).commit();
                respons = con.getResponseCode();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respons;
    }
        @Override
    protected void onPreExecute(){
            progressBar = new ProgressDialog(context);
            progressBar.setCancelable(true);
            progressBar.setMessage("Login in...");
            progressBar.show();
    }

    @Override
    protected void onPostExecute(Integer result) {
        progressBar.dismiss();
    }

}

