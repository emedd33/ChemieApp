package com.example.eskild.hc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;



/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;
    private Context mContext;
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mContext = LoginActivity.this;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

    }
    public void onClick(View v) {

        switch (v.getId()) { //finner hvilken knapp som er trykket
            case R.id.btn_login: // Button for login

                if (isNetworkAvailable()){ // Skjekker om brukeren er koblet til nettet.

                    // henter ut data som er skrevet inn i aktiviteten
                    EditText username_text = (EditText)findViewById(R.id.input_email);
                    username = username_text.getText().toString();
                    EditText password_text = (EditText)findViewById(R.id.input_password);
                    password = password_text.getText().toString();

                    // starter Backthread for login.
                    BackgroundWorker_login Worker = new BackgroundWorker_login(LoginActivity.this); //lager objectet for login
                    try {
                        // starter Loginprosessen, videreføres til Backgruondworker_login
                        int respons = Worker.execute(username, password).get();

                        if (respons==200){ // login success

                            Intent intent = new Intent(LoginActivity.this, HovedActivity.class);
                            BackgroundWorker_profile worker = new BackgroundWorker_profile(mContext);
                            worker.execute();

                            //Legger til subspriction for notifiction fra Firebase,
                            // default vil brukeren være subscriped når han logger inn.
                            FirebaseMessaging.getInstance().subscribeToTopic("Kaffe");
                            FirebaseMessaging.getInstance().subscribeToTopic("Event");
                            FirebaseMessaging.getInstance().subscribeToTopic("Felles");
                            prefs.edit().putBoolean("Kaffe",true);
                            prefs.edit().putBoolean("Event",true);
                            prefs.edit().putBoolean("Felles",true);
                            prefs.edit().putBoolean("Access",true).commit();
                            prefs.edit().commit();

                            startActivity(intent);
                            finish();

                        } else if (respons == 400) {
                            Toast.makeText(LoginActivity.this, "Feil brukernavn og passord", Toast.LENGTH_SHORT).show();
                        } else if (respons == 401){
                            Toast.makeText(LoginActivity.this, "Ikke autorisert, error: 401", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error code " +String.valueOf(respons) , Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
