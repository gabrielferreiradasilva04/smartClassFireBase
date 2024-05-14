package com.gabriel.smartclass.view;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.gabriel.smartclass.R;


public class BaseNotification extends Notification {
    private final String CHANNEL_ID = "SmartClassNotifications";
    private final String SMARTCLASSCHANNELNAME = "SmartClassChannel";
    private int id = 0;
    private NotificationManagerCompat notificationManagerCompat;

    private void createNotificationsChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, SMARTCLASSCHANNELNAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.createNotificationChannel(channel);
        }
    }

    public void trigger(String title, String contentText, Context context) {
        createNotificationsChannel(context);
        NotificationCompat.Builder builder = notificationBuild(title, contentText, context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            notificationManagerCompat.notify(id, builder.build());

        }
    }
    private NotificationCompat.Builder notificationBuild(String title, String contentText, Context context){
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
