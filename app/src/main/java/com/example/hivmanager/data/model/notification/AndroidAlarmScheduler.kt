package com.example.hivmanager.data.model.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.ZoneId
/** реализация класса для отправки уведомлений*/
class AndroidAlarmScheduler(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    /**назначение уведомления
     * */
    override fun schedule(item: AlarmItem) {
        Log.d("AlarmManager","schedule: ${item.time} - ${item.message} -${item.hashCode()}")
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
        }
        alarmManager.setExactAndAllowWhileIdle(//требуем точное время
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    /**отмена уведомления, сравнение происходит по хешкоду,
     *  хешкоды равны, если содержимые AlarmItem равны
     * */
    override fun cancel(item: AlarmItem) {
        Log.d("AlarmManager","cancel: ${item.time} - ${item.message} - ${item.hashCode()}")
        try {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }catch (e:Exception){
            Log.d("AndroidAlarmScheduler","failed to cancel alarm - ${item.message}")
        }
    }
}