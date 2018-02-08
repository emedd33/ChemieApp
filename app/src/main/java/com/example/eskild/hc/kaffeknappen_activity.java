package com.example.eskild.hc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class kaffeknappen_activity extends AppCompatActivity {
    private Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaffeknappen_activity);
        toolbar1 = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(kaffeknappen_activity.this, HovedActivity.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        AlertDialog.Builder kaffe_alert = new AlertDialog.Builder(kaffeknappen_activity.this);
        kaffe_alert.setMessage(R.string.Mekka_kaffe).setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            //When yes is pressed a notification is created
            //Notification for all coffelovers out there!
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(kaffeknappen_activity.this, 0, intent, 0);
                Notification kaffe_noti = new Notification.Builder(kaffeknappen_activity.this)
                        .setTicker("Title Ticker")
                        .setContentTitle("Title")
                        .setContentText("Melding")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent).getNotification();
                kaffe_noti.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager)kaffeknappen_activity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(0,kaffe_noti);
                dialog.cancel();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            //When no is created the dialog i just canceled
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //Creations of alert dialog
        AlertDialog alert = kaffe_alert.create();
        alert.setTitle("Sikker?");
        alert.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(kaffeknappen_activity.this, HovedActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile_image:
                Toast.makeText(kaffeknappen_activity.this, "Profile", Toast.LENGTH_SHORT).show();
                Intent profileintent = new Intent(this, myProfile.class);
                startActivity(profileintent);
                return true;
            case R.id.Settings:
                Toast.makeText(kaffeknappen_activity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
