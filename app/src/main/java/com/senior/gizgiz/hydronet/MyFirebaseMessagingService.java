package com.senior.gizgiz.hydronet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.senior.gizgiz.hydronet.Activity.MainActivity;
import com.senior.gizgiz.hydronet.HelperClass.NotificationID;
import com.senior.gizgiz.hydronet.HelperClass.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static List<String> notificationList = new ArrayList<>();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            if (true) scheduleJob();
            else handleNow(); // 10 secs
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        sendNotification(remoteMessage);
    }

    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationList.add(remoteMessage.getNotification().getBody());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("HydroNet");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
        for(String line : notificationList) inboxStyle.addLine(line);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_app_siluette)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setStyle(inboxStyle)
                        .setContentIntent(pIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setColor(ResourceManager.getColor(getBaseContext(),R.color.green))
                        .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
