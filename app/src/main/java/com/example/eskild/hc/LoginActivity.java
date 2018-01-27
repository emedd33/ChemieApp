package com.example.eskild.hc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    public static final String TAG = "MyActivity";
    private String email;
    private String password;
    private Boolean access;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.access = false;

        //Remove title bar, deaktivert for nå
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //Lager en adminkonto som kan brukes for innlogging
        //Må deaktiviseres etter at getAuthToken from getAccount fungerer

    }


    //Click på loginbutton for inlogging
    public void onLoginButton(View view) {
        Button login_btn = (Button) findViewById(R.id.login_button);
        final ProgressBar progressbar = (ProgressBar)findViewById(R.id.login_progress);

        login_btn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View v) {;
                if (isNetworkAvailable()){
                    EditText email_text = (EditText)findViewById(R.id.email_text);
                    email = email_text.getText().toString();
                    EditText password_text = (EditText)findViewById(R.id.password_text);
                    password = password_text.getText().toString();

                    Context mContext = (Context) LoginActivity.this;

                    BackgroundWorker Worker = new BackgroundWorker(mContext,progressbar);
                    try {

                        String Token = Worker.execute("LOGIN", email, password).get();
                        int respons = Worker.responsCode;
                        if (respons==200){
                            session = new Session(LoginActivity.this);
                            session.setToken(Token);
                            session.setusename(email);
                            Intent intent = new Intent(LoginActivity.this, HovedActivity.class);
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
    }

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
