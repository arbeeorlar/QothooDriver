package com.app.qothoo.driver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.app.qothoo.driver.MainActivity;
import com.app.qothoo.driver.R;
import com.app.qothoo.driver.Utilities.Constants;
import com.app.qothoo.driver.Utilities.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    public NotificationUtil notificationUtils;

    private static void generateNotification(Context context, String message) {
        int icon = R.mipmap.ic_launcher_circle;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);  //work  arround it
        // set intent so it does not start a new activity
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        // notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);//.TYPE_ALARM);//.TYPE_NOTIFICATION);
        //Vibrate if vibrate is enabled
        notification.sound = alarmSound;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());

            // generateNotification(getApplicationContext(),remoteMessage.getNotification().getBody());

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtil.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

//            // play notification sound
//            NotificationUtil notificationUtils = new NotificationUtil(getApplicationContext());
//            notificationUtils.playNotificationSound();
        } else {

            // If the app is in background, firebase itself handles the notification
        }
    }



    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        String message = "";
        String Condition;
        String others;
        try {
            //JSONObject data = json.getJSONObject("data");

            Condition = json.getString("Condition");

            if ("TripReq".contains(Condition)) {

                Log.e(TAG, "TripId TripId: " + json.getString("TripId"));


                Log.e(TAG, "TripId TripId: " + json.getString("TripId"));

                String tripId = json.getString("TripId");
                String RideTypeId = json.getString("RideTypeId");

                double latPoint = json.getDouble("Lat");
                double longPoint = json.getDouble("Long");


                message = "You have a trip Request.";
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("tripId", tripId);
                resultIntent.putExtra("RideTypeId",RideTypeId);
                resultIntent.putExtra("Condition", Condition);
                resultIntent.putExtra("latPoint",latPoint);
                resultIntent.putExtra("longPoint", longPoint);

               // createNotification(message,resultIntent);


                showNotificationMessage(getApplicationContext(), getApplicationContext().getString(R.string.app_name), message, "", resultIntent);
                NotificationUtil notificationUtils = new NotificationUtil(getApplicationContext());
               // notificationUtils.playNotificationSound();
              //  LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);

            } else if ("TripAccept".contains(Condition)) {


            }else if ("tripCancel".contains(Condition)) {


            }else if ("RiderLoc".contains(Condition)) {


            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    private void createNotification( String messageBody,Intent intent) {
        //Intent intent = new Intent( this , MainActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSoundURI =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new
                NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
