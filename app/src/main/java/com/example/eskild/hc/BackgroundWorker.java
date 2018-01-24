package com.example.eskild.hc;

import android.content.Context;
import android.media.ImageWriter;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Eskild on 29.10.2017.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    private Context context;
    private ProgressBar progressbar;

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if (type == "LOGIN"){
            //for fjordgata 17
            //String login_url = "http://192.168.20.5:8000/sladreboks/get_auth_token/";

            //Husk å skifte når du er på en annen ipv4
            String login_url = "http://192.168.1.6:8000/sladreboks/get_auth_token/";
            String user_name = params[1];
            String password = params[2];
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

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();


                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                int responseCode = con.getResponseCode();
                System.out.println("Response Code : " + responseCode);

                return String.valueOf(response);




                /*
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password", "UTF-8")+ "=" +URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = "";
                String result = "";
                while((line=bufferedReader.readLine())!= null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        /*
        } if (type == "SEND_SLADDER"){
            String url_string = "http://192.168.20.5:8000/sladreboks/rest/";
            String text = params[1];
            String Token = "Token 72cc07d3b7698f1c598f2e6c4c07614ea2e1d183";
            try {
                URL url = new URL(url_string);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", text);

                String JSON = jsonObject.toString();

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", Token);
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Type", "application/json");

                con.setDoOutput(true);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(JSON);
                writer.close();
                outputStream.close();

                int responseCode = con.getResponseCode();
                System.out.println(JSON);
                System.out.println("Response Code : " + responseCode);
                return String.valueOf(responseCode);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
       */}
        return null;
    }
    @Override
    protected void onPreExecute(){
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        progressbar.setVisibility(View.INVISIBLE);

    }
    BackgroundWorker(Context con, ProgressBar progressbar){
        this.context = con;
        this.progressbar = progressbar;
    }
}

