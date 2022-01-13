package com.ftg2021.effo2021V1.Apps.News.FirebasePushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.News.NewsMainActivity;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Apps.News.api.AccessData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import me.leolin.shortcutbadger.ShortcutBadger;

public class FirebaseRegisterToken extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "123";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        HashMap<String, String> params = new HashMap<String, String>();
        Log.d("Notification ", s);
        params.put("query", "INSERT INTO notification_tokens(token) VALUES ('" + s + "')");

        JsonObjectRequest req = new JsonObjectRequest(AccessData.getApiUrl(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Notification Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Notification Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("Notification", remoteMessage.getData().get("title"));

        AccessData.badgeCount++;

        ShortcutBadger.applyCount(getApplicationContext(),AccessData.badgeCount);

        sendNotification(remoteMessage,AccessData.badgeCount);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(20000);
//            connection.setReadTimeout(20000);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendNotification(RemoteMessage remoteMessage,int badgeCount) {
        createNotificationChannel();

        Intent intent = new Intent(this, NewsMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int newsID = Integer.parseInt(remoteMessage.getData().get("newsId"));
        intent.putExtra(NewsMainActivity.ADAPTERPOSITION, newsID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, newsID, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String newsStatus = remoteMessage.getData().get("status");
        Log.i("Notification", "status:" + newsStatus);
//        Log.i("Notification", AccessData.getBaseUrl() + "/" + remoteMessage.getData().get("imageUrl"));

        NotificationCompat.Builder mBuilder = null;
        if (newsStatus.equals("1")) {
            RemoteViews notificationLayoutNormal = new RemoteViews(getPackageName(), R.layout.custom_notification_normal);


            String titleFound = remoteMessage.getData().get("title").trim().substring(0,1);

            notificationLayoutNormal.setTextViewText(R.id.text, remoteMessage.getData().get("title"));
            mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_svg_effo_logo)
//                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                    .setCustomContentView(notificationLayout1)
                    .setContent(notificationLayoutNormal)
                    .setContentIntent(pendingIntent);
        }
        if (newsStatus.equals("5")) {
            RemoteViews notificationLayout1 = new RemoteViews(getPackageName(), R.layout.custom_notification_type1);
            notificationLayout1.setTextViewText(R.id.text, remoteMessage.getData().get("title"));
            String imageUrl = AccessData.getBaseUrl() + "/" + remoteMessage.getData().get("imageUrl");
            try {
                Bitmap bitmapImage = getBitmapFromURL(imageUrl);
                notificationLayout1.setImageViewBitmap(R.id.news_image, bitmapImage);
                if (bitmapImage != null) {
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_svg_effo_logo)
//                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                    .setCustomContentView(notificationLayout1)
                            .setContent(notificationLayout1)
                            .setContentIntent(pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (newsStatus.equals("4") || newsStatus.equals("6")) {
            RemoteViews notificationLayout2 = new RemoteViews(getPackageName(), R.layout.custom_notification_type2);
            RemoteViews notificationLayoutExpanded2 = new RemoteViews(getPackageName(), R.layout.custom_expand_notification_type2);

            notificationLayout2.setTextViewText(R.id.text, remoteMessage.getData().get("title"));
            notificationLayoutExpanded2.setTextViewText(R.id.text, remoteMessage.getData().get("title"));

            String imageUrl = AccessData.getBaseUrl() + "/" + remoteMessage.getData().get("imageUrl");
            try {
                Bitmap bitmapImage = getBitmapFromURL(imageUrl);
                notificationLayoutExpanded2.setImageViewBitmap(R.id.expand_image, bitmapImage);
                if (bitmapImage != null) {
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_svg_effo_logo)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setCustomContentView(notificationLayout2)
                            .setCustomBigContentView(notificationLayoutExpanded2)
                            .setContentIntent(pendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mBuilder != null) {
            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            ShortcutBadger.applyNotification(getApplicationContext(),notification,badgeCount);
            notificationManager.notify(newsID, notification);
        }




//        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "Rayat" ) ;
//        mBuilder.setContentTitle( remoteMessage.getData().get("title") ) ;
//        mBuilder.setContentText( remoteMessage.getData().get("body")) ;
//        mBuilder.setLargeIcon(BitmapFactory. decodeResource (getResources() , R.drawable.logo_png )) ;
//        mBuilder.setSmallIcon(R.drawable. logo_png ) ;
//        mBuilder.setAutoCancel( true ) ;
//        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
//            int importance = NotificationManager. IMPORTANCE_HIGH ;
//            NotificationChannel notificationChannel = new NotificationChannel( "Rayat" , "NOTIFICATION_CHANNEL_NAME" , importance) ;
//            mBuilder.setChannelId( "Rayat" ) ;
//            assert mNotificationManager != null;
//            mNotificationManager.createNotificationChannel(notificationChannel) ;
//        }
//        assert mNotificationManager != null;
//        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
}