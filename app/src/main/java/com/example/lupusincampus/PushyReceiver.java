package com.example.lupusincampus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import me.pushy.sdk.Pushy;

import org.json.JSONException;
import org.json.JSONObject;

public class PushyReceiver extends BroadcastReceiver {
    private static final String TAG = "PushyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // Log raw intent extras
            Log.d(TAG, "Raw Intent Extras: " + intent.getExtras());

            // Extract Pushy payload as JSON string
            String payloadString = intent.getStringExtra("pushyPayload");

            if (payloadString != null) {
                // Convert payload string to JSON object
                JSONObject payload = new JSONObject(payloadString);

                // Extract notification data
                String notificationTitle = payload.optString("title", context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString());
                String notificationText = payload.optString("message", "No message received");

                Log.d(TAG, "onReceive: notificationTitle = " + notificationTitle);
                Log.d(TAG, "onReceive: notificationText = " + notificationText);

                // Display notification
                showNotification(context, notificationTitle, notificationText);
            } else {
                Log.e(TAG, "Pushy payload is null");
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing Pushy payload", e);
        }
    }

    private void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "PushyChannel")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE));

        // Automatically configure a Notification Channel for Android O+
        Pushy.setNotificationChannel(builder, context);

        // Get NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show notification with a random ID
        notificationManager.notify((int) (Math.random() * 100000), builder.build());
    }
}
