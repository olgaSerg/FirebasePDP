package com.example.firebasepdp.helper

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.firebasepdp.R

class NotificationHelper(private val context: Context) {

    fun sendNotification(channelId: String, title: String, message: String) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
    }
}