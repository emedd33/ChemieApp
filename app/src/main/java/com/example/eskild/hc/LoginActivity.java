package com.example.eskild.hc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;



/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;
    private Boolean access;
    private Session session;
    private Context mContext;
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mContext = (Context) LoginActivity.this;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void onClick(View v) {
        switch (v.getId()) { //finner hvilken knapp som er trykket
            case R.id.login_button: // Button for login
                if (isNetworkAvailable()){ // Skjekker om brukeren er koblet til nettet.

                    // henter ut data som er skrevet inn i aktiviteten
                    EditText username_text = (EditText)findViewById(R.id.email_text);
                    username = username_text.getText().toString();
                    EditText password_text = (EditText)findViewById(R.id.password_text);
                    password = password_text.getText().toString();

                    // starter Backthread for login.
                    BackgroundWorker_login Worker = new BackgroundWorker_login(mContext); //lager objectet for login
                    try {
                        // starter Loginprosessen, videreføres til Backgruondworker_login
                        int respons = Worker.execute(username, password).get();

                        if (respons==200){ // login success

                            Session session = new Session(prefs);
                            session.setToken(Worker.getToken()); // setter token for å cashe bruker til neste
                            int result = session.setValues(); // Henter brukerinfo og legger dem til Session
                            Intent intent = new Intent(LoginActivity.this, HovedActivity.class);
                            prefs.edit().putBoolean("Access",true).commit();
                            intent.putExtra("session",(Parcelable)session);

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


    /*//Click på loginbutton for inlogging
    public void onLoginButton(View view) {
        Button login_btn = (Button) findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {;

            if (isNetworkAvailable()){
                    EditText username_text = (EditText)findViewById(R.id.email_text);
                    username = username_text.getText().toString();
                    EditText password_text = (EditText)findViewById(R.id.password_text);
                    password = password_text.getText().toString();

                    BackgroundWorker_login Worker = new BackgroundWorker_login(mContext,this.progressBar);
                    try {
                        String Token = Worker.execute("LOGIN", username, password).get();
                        int respons = Worker.responsCode;
                        if (respons==200){
                            session.setToken(Token);
                            session.setValues();
                            Intent intent = new Intent(LoginActivity.this, HovedActivity.class);
                            //intent.putExtra("session",session);
                            startActivity(intent);
                            finish();
                        } else if (respons == 400) {
                            Toast.makeText(LoginActivity.this, "Feil brukernavn og passord", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error code " +String.valueOf(respons) , Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    //lager en back thread som skjekker om brukernavn & passord gir en authorization token.
                } else {
                    Toast.makeText(LoginActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public boolean isNetworkAvailable() {
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else {
            return false;
        }

    }

}
