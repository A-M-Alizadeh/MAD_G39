package com.example.profilelab

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class FireNotification(var context: Context, var title: String, var message: String) {
    private val channelID: String = "MADFCM100"
    private val channelName: String = "FCMName100"
    private val notificationManager: NotificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder

    fun fireNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val intent = android.content.Intent(context, FriendsContainer::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder = NotificationCompat.Builder(context, channelID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_send)
            .addAction(R.drawable.ic_send, "See Requests", pendingIntent)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(100, builder.build())
    }


}