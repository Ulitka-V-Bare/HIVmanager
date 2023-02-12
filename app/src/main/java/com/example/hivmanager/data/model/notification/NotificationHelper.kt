package com.example.hivmanager.data.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hivmanager.MainActivity
import com.example.hivmanager.R

class NotificationHelper(val context:Context) {
    private val CHANNEL_ID = "HIV_manager_channel_id2"
    private val NOTIFICATION_ID = 1


    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH ).apply {
                description = "Reminder Channel Description"
            }
            channel.enableVibration(true)
            channel.setBypassDnd(true)

            //channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes(AudioAttributes.USAGE_NOTIFICATION))
            val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(title: String, message: String){
        // 1
        createNotificationChannel()
        // 2
        val intent = Intent(context, MainActivity:: class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // 3
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        // 4
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.logo_light)
        val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)
        // 5
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.logo_no_background)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(icon).bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(DEFAULT_VIBRATE_PATTERN)
           // .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        // 6
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }
}