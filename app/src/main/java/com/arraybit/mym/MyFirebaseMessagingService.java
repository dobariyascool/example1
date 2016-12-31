package com.arraybit.mym;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.arraybit.global.Globals;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String message, memberName;
    String title = "Mahavir Yuvak Mandal", notificationImage = "", type;
    int Id;
    Bitmap bitmap = null;

    public static Bitmap getBitmap(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getData().size() > 0) {

                if (remoteMessage.getData().containsKey("body")) {
                    message = remoteMessage.getData().get("body");
                }
                if (remoteMessage.getData().containsKey("title")) {
                    title = remoteMessage.getData().get("title");
                }
                if (remoteMessage.getData().containsKey("type")) {
                    type = remoteMessage.getData().get("type");
                }
                if (remoteMessage.getData().containsKey("id")) {
                    if (remoteMessage.getData().get("id") != null && !remoteMessage.getData().get("id").equals("null") && !remoteMessage.getData().get("id").equals("")) {
                        Id = Integer.parseInt(remoteMessage.getData().get("id"));
                    }
                }
                if (remoteMessage.getData().containsKey("notificationImage")) {
                    if (remoteMessage.getData().get("notificationImage") != null && !remoteMessage.getData().get("notificationImage").equals("null") && !remoteMessage.getData().get("notificationImage").equals("")) {
                        notificationImage = remoteMessage.getData().get("notificationImage");
                        bitmap = getBitmap(notificationImage);
                    }
                }
                if (remoteMessage.getData().containsKey("memberName")) {
                    if (remoteMessage.getData().get("memberName") != null && !remoteMessage.getData().get("memberName").equals("null") && !remoteMessage.getData().get("memberName").equals("")) {
                        memberName = remoteMessage.getData().get("memberName");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sendNotification();
        }
    }

    private void sendNotification() {
        try {
            Intent intent;
            if (type.equals("Request")) {
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("isStart", true);
                intent.putExtra("memberMasterId", Id);
                intent.putExtra("isNotification", true);
                intent.putExtra("memberName", memberName);
            } else if (type.equals("Notification")) {
                intent = new Intent(getApplicationContext(), NotificationActivity.class);
                intent.putExtra("isStart", true);
            } else if (type.equals("Approved")) {
                Globals.ClearUserPreference(this);
                intent = new Intent(getApplicationContext(), SignInActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), NotificationActivity.class);
                intent.putExtra("isStart", true);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder notificationBuilder;
            if (bitmap != null) {
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.mandal_logo))
                        .setSmallIcon(R.mipmap.mandal_logo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(Picasso.with(MyFirebaseMessagingService.this).load(notificationImage).get())
                                .setSummaryText(message))
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setVibrate(new long[]{100, 400, 100, 400})
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);
            } else {
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.mandal_logo))
                        .setSmallIcon(R.mipmap.mandal_logo)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setVibrate(new long[]{100, 400, 100, 400})
                        .setContentIntent(pendingIntent);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}