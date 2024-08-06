package com.example.mywidgetapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessageReceiver : FirebaseMessagingService() {

    companion object {
        private const val default_channel_id = "default_channel_id"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "From Data: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            handleDataPayload(remoteMessage.data)

        }
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body:")
            sendNotification(it.body)
        }
    }


    private fun sendNotification(messageBody: String?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, default_channel_id).setSmallIcon(R.drawable.icon_flat)
                .setContentTitle("Data Updated : ").setContentText(messageBody).setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                default_channel_id, default_channel_id, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


    private fun handleDataPayload(data: Map<String, String>) {

        val value1 = data["bodyText"] // Retrieve value for key1
        if (value1 != null) {
            sendNotification(value1)
            UpdateWidgetData(this).updateWidget(value1, true)
        }


    }
}