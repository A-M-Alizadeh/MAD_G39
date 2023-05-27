package com.example.profilelab.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.profilelab.FriendsContainer
import com.example.profilelab.R
import java.util.*
import javax.inject.Inject


class MyNotificationManager @Inject constructor(private val mCtx: Application) {



    fun textNotification(title: String?, message: String?) {
        val rand = Random()
        val idNotification = rand.nextInt(1000000000)

//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =  mCtx.applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Channel_id_default", "Channel_name_default", NotificationManager.IMPORTANCE_HIGH
            )
//            val attributes = AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()

//            notificationChannel.description = "Channel_description_default"
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
//            notificationChannel.setSound(soundUri, attributes)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(mCtx, "Channel_id_default")
        val intent = android.content.Intent(mCtx, FriendsContainer::class.java)
        val pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationBuilder
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_send)
            .addAction(R.drawable.ic_send, "See Requests", pendingIntent)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(idNotification, notificationBuilder.build())
    }


}