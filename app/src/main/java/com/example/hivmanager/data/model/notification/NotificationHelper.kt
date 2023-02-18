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
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hivmanager.MainActivity
import com.example.hivmanager.R
import com.example.hivmanager.data.model.Constants.CHANNEL_ID
import com.example.hivmanager.data.model.PillInfo
import java.time.LocalDateTime

/** класс содержит функции для создания уведомления
 * */
class NotificationHelper(val context:Context) {

    private val NOTIFICATION_ID = 1

        /**андроид системы, версии которых больше или равны 26 требуют создания канала уведомлений
         * */
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH ).apply {
                description = "Reminder Channel Description"
            }
            channel.enableVibration(true)
            channel.setBypassDnd(true)
            channel.vibrationPattern = longArrayOf(0, 250, 250, 250)
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,audioAttributes)


            //channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes(AudioAttributes.USAGE_NOTIFICATION))
            val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    /**создавалась для получения существующего канала и проверки на то, включены ли уведомления,
     * не используется
     * */
    fun getNotificationChannel():NotificationChannel{
        val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.getNotificationChannel(CHANNEL_ID)
    }
    /** создание и отображение уведомления с заданным заголовком и сообщением
     * */
    fun createNotification(title: String, message: String){
        // 1
        createNotificationChannel()
        // 2
        val intent = Intent(context, MainActivity:: class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // 3
        val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(context, 0, intent, 0)
        // 4
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.logo_light)
        val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)
        // 5
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.logo_no_background)
            //  .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(DEFAULT_VIBRATE_PATTERN)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        // 6
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }
    /** функция создаст уведомления из экземпляра pillInfo и выставит их в нужное время
     * */
    fun createNotificationRequest(pillInfo: PillInfo,scheduler:AlarmScheduler) {
        for (time in pillInfo.timeToTakePill) {
            val date = pillInfo.startDate.split('.')
            val firstDayToNotify = LocalDateTime.now()
                .withYear(date[2].toInt())
                .withMonth(date[1].toInt())
                .withDayOfMonth(date[0].toInt())
                .withHour(time.split(':')[0].toInt())
                .withMinute(time.split(':')[1].toInt())
                .withSecond(0)
                .withNano(0)
            Log.d("AddPillVM", "$firstDayToNotify")
            for (i in 0 until pillInfo.duration) {
                val alarmItem = AlarmItem(
                    firstDayToNotify.plusDays(i.toLong()),
                    "время принять: ${pillInfo.name}"
                )
                if(alarmItem.time>LocalDateTime.now())
                    alarmItem.let(scheduler::schedule)
            }
        }
    }
    /** функция удалит уведомления, связанные с экземпляром pillInfo
     * */
    fun cancelNotifications(pillInfo: PillInfo, scheduler: AlarmScheduler){
        for(time in pillInfo.timeToTakePill){
            Log.d("cancelPill","${pillInfo.startDate} - ${time}")
            val year = pillInfo.startDate.split('.')[2].toInt()
            val month =pillInfo.startDate.split('.')[1].toInt()
            val day = pillInfo.startDate.split('.')[0].toInt()
            val hour = time.split(':')[0].toInt()
            val minute = time.split(':')[1].toInt()
            val firstDayToNotify = LocalDateTime.of(
                year,
                month,
                day,
                hour,
                minute,
                0,
                0
            )
            Log.d("cancelPill","$firstDayToNotify")
            //val firstDayToNotify = LocalDateTime.now().withHour(time.split(':')[0].toInt()).withMinute(time.split(':')[1].toInt())
            for(i in 0 until pillInfo.duration){
                val alarmItem = AlarmItem(firstDayToNotify.plusDays(i.toLong()),"время принять: ${pillInfo.name}")
                alarmItem.let(scheduler::cancel)
            }
        }
    }
}