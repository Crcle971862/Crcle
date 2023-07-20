package com.example.crcle;

//import static com.example.crcle.Cloud_notification.Fcm_notification.Comments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.LogPrinter;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Fcm_message_service extends FirebaseMessagingService {
    public static final String Example="example";
    String title,body;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title=message.getData().get("title");
        String body=message.getData().get("body");
        Log.d("manish",title);
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification mBuilder;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            mBuilder=new Notification.Builder(this,Example)
                    .setSmallIcon(R.drawable.face)
                    .setContentTitle(title)
                    .setContentText(body)
                    //.setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(Example,"new channel",NotificationManager.IMPORTANCE_HIGH));
        }
        else {
            mBuilder=new Notification.Builder(this,Example)
                    .setSmallIcon(R.drawable.face)
                    .setContentTitle(title)
                    .setContentText(body)
                    //.setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(Example,"new channel",NotificationManager.IMPORTANCE_HIGH));
        }
       // NotificationManagerCompat.from(this).notify(101,mBuilder);
        notificationManager.notify(100,mBuilder);


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
