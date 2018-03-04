package com.example.eskild.hc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by Eskild on 12.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public Notification kaffe_noti;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        Log.d(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String content = remoteMessage.getNotification().getBody();
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            if (remoteMessage.getFrom().equals("/topics/Kaffe")){
                this.kaffe_noti = new Notification.Builder(this)
                        .setContentTitle("Chemie")
                        .setContentText(content)
                        .setSmallIcon(R.drawable.notification_coffee)
                        .setContentIntent(pendingIntent)
                        .getNotification();
                kaffe_noti.defaults |= Notification.DEFAULT_VIBRATE;
                kaffe_noti.defaults |= Notification.DEFAULT_SOUND;
                kaffe_noti.flags = Notification.FLAG_AUTO_CANCEL;
            } else {
                this.kaffe_noti = new Notification.Builder(this)
                        .setContentTitle("Chemie")
                        .setContentText(content)
                        .setSmallIcon(R.drawable.notification_main)
                        .setContentIntent(pendingIntent).getNotification();
                kaffe_noti.defaults |= Notification.DEFAULT_VIBRATE;
                kaffe_noti.defaults |= Notification.DEFAULT_SOUND;
                kaffe_noti.flags = Notification.FLAG_AUTO_CANCEL;
            }
            NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0,kaffe_noti);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
