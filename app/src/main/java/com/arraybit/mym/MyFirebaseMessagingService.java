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
import android.util.Log;

import com.arraybit.global.Globals;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String message, memberName;
    String title = "Mahavir Yuvak Mandal", notificationImage = "", type;
    int Id;
    Bitmap largeBitmap = null;
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
        //Displaying data in log
        //It is optional

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
            Log.e("error", " " + e.getMessage());
            e.printStackTrace();
        } finally {
            sendNotification();
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
//    private void sendNotification(String messageBody) {
//        try {
//            JSONObject json = new JSONObject(messageBody);
//            message = json.getString("message");
//            tickerText = json.getString("tickerText");
//            contentTitle = json.getString("contentTitle");
//            notificaiontype = json.getInt("notificationType");
//            if (notificaiontype == 0) {
//                offeMasterId = json.getInt("Id");
//            } else if (notificaiontype == 1) {
//                ItemMasterId = json.getInt("Id");
//            }
//            String largeIconUrl = json.getString("largeIcon"); // the way you obtain this may differ
//
//            largeBitmap = Glide
//                    .with(this)
//                    .load(largeIconUrl)
//                    .asBitmap()
//                    .into(100, 100) // Width and height
//                    .get();
//
//            Intent intent;
//            int requestCode = 0;
//            if (notificaiontype == 2) {
//                intent = new Intent(this, OfferDetailActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("OfferMasterId", offeMasterId);
//            } else if (notificaiontype == 1) {
//                intent = new Intent(this, DetailActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("isBannerClick", true);
//                intent.putExtra("ItemMasterId", ItemMasterId);
//            } else {
//                intent = new Intent(this, NotificationActivity.class);
//                intent.putExtra("isStart", true);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
//
//            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        Bitmap aBigBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
//            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setContentTitle(contentTitle)
//                    .setContentText(message)
//                    .setTicker(tickerText)
//                    .setAutoCancel(true)
//                    .setSound(sound)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo_likeat))
//                    .setVibrate(new long[]{100, 250, 100, 250})
//                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeBitmap))
//                    .setPriority(Notification.PRIORITY_MAX)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void sendNotification() {
        try {
            Intent intent;
//            if (type == Globals.BannerType.Offer.getValue()) {
            if (type.equals("Request")) {
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("isStart", true);
                intent.putExtra("memberMasterId", Id);
                intent.putExtra("isNotification", true);
                intent.putExtra("memberName", memberName);
//            } else if (type == Globals.BannerType.Item.getValue()) {
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
                Log.e("message", " " + message);
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
                Log.e("message1", " " + message);
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
            Log.e("error", " " + e.getMessage());

        }
    }
}