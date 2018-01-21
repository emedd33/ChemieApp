package com.example.eskild.hc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            @Override
            public void onClick(View v) {;
                if (isNetworkAvailable()){
                    EditText email_text = (EditText)findViewById(R.id.email_text);
                    email = email_text.getText().toString();
                    EditText password_text = (EditText)findViewById(R.id.password_text);
                    password = password_text.getText().toString();

                    //lager en back thread som skjekker om brukernavn & passord gir en authorization token.
                    loginWithNetworkAvailable(progressbar);
                } else {
                    Toast.makeText(LoginActivity.this, "Network is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginWithNetworkAvailable(ProgressBar progressbar){
        Context mContext = (Context) this;
        BackgroundWorker backgroundWorker = new BackgroundWorker(mContext, progressbar);
        String result = null;
        try {
            if (email.equals("admin") && password.equals("admin")) {
                result = "success";
            } else {
                result = backgroundWorker.execute("LOGIN", email, password).get();
            }
            if (result.equals("success")) {
                //
                Intent intent = new Intent(LoginActivity.this, HovedActivity.class);
                Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Feil brukernavn/passord", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else {
            return false;
        }

    }
}
