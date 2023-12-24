package com.example.boilerplate.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.boilerplate.R

class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationId = intent?.getIntExtra("notification_id", 0)
        val message = intent?.getStringExtra("notification_message")
        if (notificationId != null && message != null) {
            // Build and show the notification using NotificationManager
            Log.d("AlarmReceiver", "onReceive")
            var builder = NotificationCompat.Builder(context, "your_channel_id")
                .setSmallIcon(R.drawable.rounded_ecg_heart_24)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(0, builder.build())
            }
        }
    }

}