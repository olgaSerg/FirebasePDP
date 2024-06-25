package com.example.firebasepdp.firebase

import com.example.firebasepdp.helper.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body
        val channelId = remoteMessage.notification?.channelId

        if (title != null && message != null && channelId != null) {
            val notificationHelper = NotificationHelper(this)
            notificationHelper.sendNotification(channelId, title, message)
        }
    }
}